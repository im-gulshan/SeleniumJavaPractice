package Karate_Framework.FeatureFiles;

import com.intuit.karate.junit5.Karate;

public class Runner2Test {

    @Karate.Test
    Karate testOne(){
       return Karate.run("classpath:Karate_Framework/FeatureFiles/CreateAirline.feature");
    }
}
