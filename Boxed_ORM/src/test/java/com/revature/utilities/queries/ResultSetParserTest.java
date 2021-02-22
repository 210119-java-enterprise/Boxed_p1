package com.revature.utilities.queries;

import com.revature.Boxed.repository.ResultSetParser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ResultSetParserTest {

    String[][] result = { { "column1", "column2" }, { "column1", "column2" } };

    @InjectMocks
    @Spy
    private ResultSetParser sut;

    @Mock
    private ResultSet resultSet;

    @BeforeTest
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void beforeMethod() throws SQLException {

    }


    ResultSet rs = mock(ResultSet.class);

//    @Test
//    public void test_getSummary_whenGiven_ResultSet(){
//        //Arrange
//
//        //Act
//        String summary = ResultSetParser.getSummary(rs);
//
//        //Assert
//        assertNotNull(summary);
//    }
}
