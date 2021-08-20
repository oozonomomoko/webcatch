package nogi.web.webcatch.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RunStatusResponse extends BaseResponse{
    private List<String> logs;
    private boolean running;
}
