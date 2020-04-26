package com.team.app.backend.dto.questions;

import com.team.app.backend.dto.options.OptionDto;

import java.util.List;

public class QuestionOptAddDto extends QuestionDto{

    private List<OptionDto> options;

    public List<OptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDto> options) {
        this.options = options;
    }
}
