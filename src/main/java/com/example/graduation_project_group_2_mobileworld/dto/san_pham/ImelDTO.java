package com.example.graduation_project_group_2_mobileworld.dto.san_pham;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImelDTO {
    private Integer id;         // ID tự gen, không cần gửi khi update
    private String imel;       // Chỉ cần trường này khi update
    private Boolean deleted;   // Không cần gửi khi update

    public ImelDTO(Integer id, String imel, Boolean deleted) {
        this.id = id;
        this.imel = imel;
        this.deleted = deleted;
    }

    public ImelDTO() {

    }
}