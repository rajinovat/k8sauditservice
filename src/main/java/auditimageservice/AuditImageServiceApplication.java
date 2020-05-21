package auditimageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.util.ClientBuilder;
import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@RestController
public class AuditImageServiceApplication {
  @RequestMapping("/")
  @GetMapping
  public static  Map<String, String> home() {
    // loading the in-cluster config, including:
    //   1. service-account CA
    //   2. service-account bearer-token
    //   3. service-account namespace
    //   4. master endpoints(ip, port) from pre-set environment variables

    HashMap<String, String> map = new HashMap<>();

    try{
        // file path to your KubeConfig
    // String kubeConfigPath = "/Users/rajesh.ramasamy/.kube/config";  

    ApiClient client = ClientBuilder.cluster().build();

    // loading the out-of-cluster config, a kubeconfig from file-system
    // ApiClient client =
    //     ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

    // set the global default api-client to the in-cluster one from above
    Configuration.setDefaultApiClient(client);

    // the CoreV1Api loads default api-client from global configuration.
    CoreV1Api api = new CoreV1Api();

    // invokes the CoreV1Api client
    V1PodList plist =
        api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);

    for (V1Pod item : plist.getItems()) {

      V1PodSpec pspec = 
               item.getSpec(); 
               for (V1Container v1Container : pspec.getContainers()) {
                map.put(v1Container.getName(),v1Container.getImage());
              }                
    }
    }catch (Exception e)
    {
	      e.printStackTrace();
    }
    return map;
  }

  public static void main(String[] args) {
    SpringApplication.run(AuditImageServiceApplication.class, args);
  }

}

