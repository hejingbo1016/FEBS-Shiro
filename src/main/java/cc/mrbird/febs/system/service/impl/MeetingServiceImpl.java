package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.dto.ResponseDTO;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FileHepler;
import cc.mrbird.febs.common.utils.Snowflake;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.SpringContextUtil;
import cc.mrbird.febs.system.constants.AdminConstants;
import cc.mrbird.febs.system.entity.File;
import cc.mrbird.febs.system.entity.*;
import cc.mrbird.febs.system.mapper.FileMapper;
import cc.mrbird.febs.system.mapper.MeetingHotelMapper;
import cc.mrbird.febs.system.mapper.MeetingMapper;
import cc.mrbird.febs.system.service.IMeetingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:11
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements IMeetingService {

    private final MeetingMapper meetingMapper;
    private final FileMapper fileMapper;
    private final MeetingHotelMapper meetingHotelMapper;
    @Value("${minio.imageUrl}")
    private String imgUrl = ""; //读取图片保存路径
    @Value("${minio.imageShowUrl}")
    private String imageShowUrl = ""; //读取图片保存路径

    private static Snowflake snowflake = Snowflake.getInstanceSnowflake();


    @Override
    public IPage<Meeting> findMeetings(QueryRequest request, Meeting meeting) {
        LambdaQueryWrapper<Meeting> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Meeting> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(baseMapper.countMeeting(meeting));
        setselectMeeting(queryWrapper, meeting);
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, true);
        return this.page(page, queryWrapper);
    }

    /**
     * 设置查询条件
     *
     * @param queryWrapper
     * @param meeting
     */
    private void setselectMeeting(LambdaQueryWrapper<Meeting> queryWrapper, Meeting meeting) {

        if (StringUtils.isNotBlank(meeting.getMeetingName())) {
            queryWrapper.like(Meeting::getMeetingName, meeting.getMeetingName());
        }
        if (StringUtils.isNotBlank(meeting.getSponsor())) {
            queryWrapper.like(Meeting::getSponsor, meeting.getSponsor());
        }
        if (StringUtils.isNotBlank(meeting.getOrganizer())) {
            queryWrapper.like(Meeting::getOrganizer, meeting.getOrganizer());
        }
        if (StringUtils.isNotBlank(meeting.getMeetingAddress())) {
            queryWrapper.like(Meeting::getMeetingAddress, meeting.getMeetingAddress());
        }
        if (StringUtils.isNotBlank(meeting.getMeetingPrincipal())) {
            queryWrapper.like(Meeting::getMeetingPrincipal, meeting.getMeetingPrincipal());
        }
        if (meeting.getStatus() != null) {
            queryWrapper.eq(Meeting::getStatus, meeting.getStatus());
        }
    }

    @Override
    public List<Meeting> findMeetings(Meeting meeting) {
        LambdaQueryWrapper<Meeting> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMeeting(Meeting meeting) {
        this.save(meeting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMeeting(Meeting meeting) {
        this.saveOrUpdate(meeting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMeeting(Meeting meeting) {
        LambdaQueryWrapper<Meeting> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }

    @Override
    public void deleteMeetings(String meetingIds) {
        List<String> list = Arrays.asList(meetingIds.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<Meeting>().lambda().in(Meeting::getId, list));

    }

    @Override
    public void auditMeeting(Meeting meeting) {
        this.meetingMapper.auditMeeting(meeting);
    }

    @Override
    public IPage<Meeting> weChatMettingList(QueryRequest request, Meeting meeting) {
        if (org.springframework.util.StringUtils.isEmpty(meeting.getStatus())) {
            meeting.setStatus(AdminConstants.AUDIT_T_TYPE);
        }
        return findMeetings(request, meeting);
    }

    @Override
    public Meeting getWeChatMettingById(Long id) {
        Meeting meeting = meetingMapper.selectById(id);
        return meeting;
    }


    @Override
    public List<HotelName> weChatHotelsByMeetingId(Long id) {
        List<HotelName> vos = new ArrayList<>();
        List<HotelName> list = meetingMapper.weChatHotelsByMeetingId(id);
        if (!Objects.isNull(list) && list.size() > 0) {
            vos = list.stream().map(t -> {
                List<MeetingHotel> rooms = new ArrayList<>();
                List<MeetingHotel> others = new ArrayList<>();
                t.setMeetingId(id);
                //通过酒店id查图片
                List<File> files = fileMapper.selectFileByHotelId(t.getHotelId());
                List<File> fileList = files.stream().map(f -> {
                    FileHepler.getFileVo(f, imageShowUrl, imgUrl);
                    return f;
                }).collect(Collectors.toList());
                //通过会议id和酒店id查费用项
                List<MeetingHotel> roomList = meetingHotelMapper.selectFeeLists(id, t.getHotelId());
                //封装所有类型为1的房间的费用项，其他则是其他费用项
                roomList.forEach(r -> {
                    if (AdminConstants.AUDIT_T_TYPE.equals(r.getFeeType())) {
                        rooms.add(r);
                    } else {
                        others.add(r);
                    }
                });
                t.setRoomList(rooms);
                t.setOtherList(others);
                t.setFileList(fileList);
                return t;
            }).collect(Collectors.toList());
        }
        return vos;
    }

    @Override
    public ResponseDTO generateQRCode(GenerateQRCodeDTO generateQRCodeDTO) {
        String fileName = generateQRCodeDTO.getId() + ".jpg";
        RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate");
        Object o = redisTemplate.opsForValue().get(fileName);
        if (o != null) {
            return new ResponseDTO(200, "", o);
        }

        try {
            Hashtable hints = new Hashtable();
            //指定纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

            String content = "http://knightmedia.ltd/booking?id=" + generateQRCodeDTO.getId();
            log.debug(content);
            //生成二维码矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 350, 350, hints);

            //获取矩阵宽度、高度,生成二维码图片
            BufferedImage image = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 350; x++) {
                for (int y = 0; y < 350; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            String url = saveFile(input);
            redisTemplate.opsForValue().set(fileName, url);
            return new ResponseDTO(200, "", url);

        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        return ResponseDTO.failture();
    }

    private String saveFile(InputStream is) {
        long id = snowflake.nextId();
        java.io.File fil = new java.io.File("/www/server/nginx/imge/" + id);
//        java.io.File fil = new java.io.File("F:\\w\\" + id);
        if (!fil.exists()) {
            fil.mkdir();
        } else {
            return null;
        }
        try (FileOutputStream fos = new FileOutputStream(fil.getAbsolutePath() + "/" + id + ".jpg");) {
            byte bytes[] = new byte[1024];
            int temp = 0;
            while ((temp = is.read(bytes)) != -1) {
                fos.write(bytes, 0, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return "F:\\w\\"  + id + "\\" +id+".jpg";
        return "http://knightmedia.ltd:9090/" + id + "/" + id + ".jpg";
    }
}
