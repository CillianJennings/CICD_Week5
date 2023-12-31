package ie.atu.cicd_week5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class FeignController {

    private final FeignService feignService;

    public FeignController(FeignService feignService){
        this.feignService = feignService;
    }

    @GetMapping("/feign")
    public String getFeignData() throws Exception, InterruptedException {
        long startTime = System.currentTimeMillis();

        List<CompletableFuture<TodoResponse>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            CompletableFuture<TodoResponse> future = CompletableFuture.supplyAsync(() -> feignService.fetchData());
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allOf.get();
        long endTime = System.currentTimeMillis();

        return "Total Execution Time: " + (endTime - startTime) + " ms";
    }

    @GetMapping("/todos")
    public Object Todos(){
        return feignService.fetchData();
    }
}
