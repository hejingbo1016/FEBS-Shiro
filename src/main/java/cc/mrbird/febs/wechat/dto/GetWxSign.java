package cc.mrbird.febs.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetWxSign {
    // 路径
    @NotNull
    private String url;
}
