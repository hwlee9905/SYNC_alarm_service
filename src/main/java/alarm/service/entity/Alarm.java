package alarm.service.entity;

import java.util.UUID;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import alarm.service.dto.AlarmDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alarm")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm extends BaseEntity{
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "alarm_id", columnDefinition = "BINARY(16)")
	@JdbcTypeCode(SqlTypes.BINARY)
	private UUID alarmId;
	
	@Column(name = "user_id", nullable = false)
	private long userId;
	
	@Column(name = "message")
	private String message;

	public AlarmDto toDto() {
		return AlarmDto.builder()
				.alarmId(alarmId)
				.userId(userId)
				.message(message)
				.build();
	}
	
}
