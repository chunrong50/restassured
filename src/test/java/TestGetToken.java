import com.training.restassured.WeWork;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;


public class TestGetToken {



@BeforeAll
public  static  void beforeAll(){


}



    @Test
    public  void  testGetToken(){
        String  token=WeWork.getAccess_token();
        assertThat( token ,not(equalTo(null)));
    }

}
