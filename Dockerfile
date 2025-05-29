# Dockerfile
FROM rabbitmq:4-management

# 호스트에 미리 내려받은 .ez 파일을 컨테이너에 복사
ADD rabbitmq_delayed_message_exchange-4.1.0.ez /opt/rabbitmq/plugins/

# 플러그인 활성화
RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange