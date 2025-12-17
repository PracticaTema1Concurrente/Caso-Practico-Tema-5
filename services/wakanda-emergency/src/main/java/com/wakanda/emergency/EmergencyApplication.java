@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients // Necesario para que funcione la inyección
public class EmergencyApplication {
    public static void main(String[] args) { SpringApplication.run(EmergencyApplication.class, args); }
}