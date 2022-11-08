package com.webank.truora.bcos3runner;

import lombok.Data;
import org.fisco.bcos.sdk.v3.model.EventLog;

/*在event处理的上下文中，封装几个常用的对象，免得修改接口时，需要对接口改来改去*/
@Data
public class Bcos3EventContext {
    Bcos3EventRegister eventRegister;
    EventLog eventLog;
    Object eventResponse ; //关联到各种event的方法定义，解析成java object，使用纯泛型，由各处理器自行理解
}
