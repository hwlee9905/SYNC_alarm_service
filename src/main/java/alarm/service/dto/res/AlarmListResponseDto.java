package alarm.service.dto.res;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

@Data
public class AlarmListResponseDto {
	private Long userId;
	private UUID alarmId;
	private String meesage;
	private Timestamp createdAt;
}
