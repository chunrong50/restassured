
import com.training.restassured.WeworkConfig;
import org.junit.jupiter.api.Test;

public class TestLoad {

    @Test
    public  void testLoad(){
      System.out.println( WeworkConfig.load("/conf/WeworkConfig.yaml").corpid) ;
    }




}
