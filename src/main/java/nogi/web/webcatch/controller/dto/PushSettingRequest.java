package nogi.web.webcatch.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PushSettingRequest {
    private Map<String, String> configs;
}
