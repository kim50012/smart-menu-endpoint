package com.basoft.eorder;

import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.application.framework.*;
import com.basoft.eorder.interfaces.command.admin.AdminCommandHandler;
import com.basoft.eorder.interfaces.command.admin.SaveCategory;
import com.basoft.eorder.interfaces.command.admin.SaveOption;
import com.basoft.eorder.util.UidGenerator;
import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

public class EOrderCommandHandlerTest {


//    @Test
    public void testSaveOption(){

        final BaseService mock = Mockito.mock(BaseService.class);
        Mockito.when(mock.getOption(Mockito.isA(Long.class))).thenReturn(
                new Option.Builder()
                        .id(new Long(0))
                        .name("root-option")
                        .chnName("root-option")
                        .build()
        );


        Mockito.when(mock.saveOption(Mockito.isA(Option.class))).then(new Answer<Option>() {
            @Override
            public Option answer(InvocationOnMock invocationOnMock) throws Throwable {
                Option cc =  (Option) invocationOnMock.getArguments()[0];
                TestCase.assertEquals(cc.name(),"test-case");
                TestCase.assertEquals(cc.id(),new Long(2));
                TestCase.assertEquals(cc.chnName(),"张三");
                return cc;

            }
        });

        UidGenerator uidGenerator = new UidGenerator();
        AdminCommandHandler ssh = new AdminCommandHandler(mock,uidGenerator);

        CommandHandleEngine engine = new DefaultCommandHandlerEngine.Builder()
                .setCommand(SaveOption.NAME,SaveOption.class)
                .setCommandHandler(new MultiCommandHandler(ssh))
                .build();

        SaveOption sc = new SaveOption();
        sc.setId(new Long(2));
        sc.setName("test-name");
        sc.setChnName("张三");


        final CommandHandlerContext context = engine.newCommandHandlerContext(SaveOption.NAME);
        try {
            engine.getCommandHandler().exec(sc,context);

        } catch (CommandHandleException e) {
            e.printStackTrace();
        }

    }

//    @Test
    public void testSaveStore(){


        final BaseService mock = Mockito.mock(BaseService.class);
        Mockito.when(mock.getCategory(new Long(1))).thenReturn(
                new Category.Builder()
                        .id(new Long(1))
                        .name("test-category")
                        .parent(null)
                        .build()
        );

        Mockito.when(mock.saveCategory(Mockito.isA(Category.class))).then(new Answer<Category>() {
            @Override
            public Category answer(InvocationOnMock invocationOnMock) throws Throwable {
                Category cc =  (Category) invocationOnMock.getArguments()[0];
                TestCase.assertEquals(cc.name(),"test-case");
                TestCase.assertEquals(cc.getParent().id(),new Long(1));
                return cc;

            }
        });


        UidGenerator uidGenerator = new UidGenerator();
        AdminCommandHandler ssh = new AdminCommandHandler(mock,uidGenerator);


        CommandHandleEngine engine = new DefaultCommandHandlerEngine.Builder()
                    .setCommand(SaveCategory.NAME,SaveCategory.class)
                    .setCommandHandler(new MultiCommandHandler(ssh))
                    .build();

        SaveCategory sc = new SaveCategory();
        sc.setName("test-case");
        sc.setChnName("菜单");
        sc.setParentId(new Long(1));


        final CommandHandlerContext context = engine.newCommandHandlerContext(SaveCategory.NAME);
        try {
            System.out.print("called \n\n");

            engine.getCommandHandler().exec(sc,context);
//            Mockito.verify(mock).saveCategory()

        } catch (CommandHandleException e) {
            e.printStackTrace();
        }

    }

//    @Test
    public void TestOrget(){
        List<Integer> list =  Arrays.asList(10,20,30);
        //通过reduce方法得到一个Optional类
        int a =  list.stream().reduce(Integer::sum).orElse(get("a"));
        int b =  list.stream().reduce(Integer::sum).orElseGet(() -> get("b"));

        System.out.println("a  "+a);
        System.out.println("b  "+b);
    }

    public  int get(String name){
        System.out.println(name+"执行了方法");
        return 1;
    }
}
