package cc.mrbird.febs.system.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GenerateQRCodeDTO {

    @NotBlank
    private Long id;
}
