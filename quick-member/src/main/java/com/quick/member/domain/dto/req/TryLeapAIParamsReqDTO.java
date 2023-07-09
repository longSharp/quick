package com.quick.member.domain.dto.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TryLeapAIParamsReqDTO {
    /**
     * 用于推理的提示。
     */
    private String prompt;
    /**
     * 用于推理的否定提示。
     */
    private String negativePrompt;
    /**
     * 用于推理的模型的版本。如果未提供，则默认为最新。
     */
    private String version;
    /**
     * 用于推理的步骤数。
     */
    private Integer steps;
    /**
     * 用于推断的图像的宽度。必须是8的倍数。
     */
    private Integer width;
    /**
     * 用于推断的图像的高度。必须是8的倍数。
     */
    private Integer height;
    /**
     * 为推理生成的图像数。最大批量大小为20。
     */
    private Integer numberOfImages;
    /**
     * 提示强度越高，生成的图像就越接近提示。必须介于0和30之间。
     */
    private Integer promptStrength;
    /**
     * 用于推理的种子。必须是正整数。
     */
    private Integer seed;
    /**
     * 训练模型时将调用的可选webhook URL。
     */
    private String webhookUrl;
    /**
     * （可选）将人脸恢复应用于生成的图像。这将使人脸图像看起来更逼真。
     */
    private boolean restoreFaces;
    /**
     * （可选）自动增强提示以生成更好的结果。
     */
    private boolean enhancePrompt;
    /**
     * （可选）升级生成的图像。这将使图像看起来更逼真。默认值为x1，这意味着没有放大。最大为x4。
     */
    private String upscaleBy;
    /**
     * 选择用于推理的采样器。
     */
    private String sampler;
}
