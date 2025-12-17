@SpringBootApplication
@EnableEurekaClient
@EnableHystrix // Activa el Circuit Breaker
public class TrafficApplication {
    public static void main(String[] args) { SpringApplication.run(TrafficApplication.class, args); }
}