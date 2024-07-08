package alarm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import alarm.service.dto.req.AlarmListRequestDto;
import alarm.service.dto.res.AlarmListResponseDto;
import alarm.service.entity.Alarm;
import alarm.service.repository.AlarmRepository;
import alarm.service.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AlarmService {
	// kafka
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final KafkaAdmin kafkaAdmin;
	private final KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory;

	// reposotiry
	private final AlarmRepository alarmRepository;

	// common
//	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;
	

	/*
	 * 유저 알람
	 */
//	@KafkaListener(topics = "User", containerFactory = "kafkaListenerContainerFactory")
//	public void userAlarm(String message) {
//		Map<String, Object> map;
//		try {
//			map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {
//			});
//		} catch (Exception e) {
//			throw new RuntimeException("시스템 오류");
//		}
//		String url = String.valueOf(map.get("url"));
//
//		messagingTemplate.convertAndSend("/topic/user/" + url, message);
//	}
	
	@KafkaListener(topics = "reqAlarmList", containerFactory = "kafkaListenerContainerFactory")
	public void getAlarmList(String message) {
		List<AlarmListResponseDto> result = new ArrayList<>();
		AlarmListRequestDto body = null;
		try {
			body = objectMapper.readValue(message, AlarmListRequestDto.class);
		} catch (Exception e) {
			// Object mapper로 변환 불가능한 경우 처리
			e.printStackTrace();
		}
		
		List<Alarm> alarmList = alarmRepository.findByUserId(body.getUserId());
		
		if (alarmList.size() > 0) {
			for (Alarm entity : alarmList) {
				
				AlarmListResponseDto dto = new AlarmListResponseDto();
				dto.setUserId(entity.toDto().getUserId());
				dto.setAlarmId(entity.toDto().getAlarmId());
				dto.setMeesage(entity.toDto().getMessage());
				dto.setCreatedAt(entity.getCreatedAt());
				
				result.add(dto);
			}
		}
//		return ResponseMessage.builder().value(result).build();
		sendResAlarm("resAlarmList", ResponseMessage.builder().value(result).build());
	}

	public void sendResAlarm(String topic, ResponseMessage message) {
		String mapper = null;
		try {
			mapper = objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			// Object mapper로 변환 불가능한 경우 처리
			e.printStackTrace();
		}
		System.out.println("★ topic ★ : " + topic);
		System.out.println("★ 내용물 ★ : " + mapper);
		kafkaTemplate.send(topic, mapper);
	}

}
