package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.*;

import java.util.ArrayList;
import java.util.List;

public class ReactorMain {
    public static void main(String[] args) {
        List<String> filterWords=new ArrayList<>();
        for(int i=2; i<args.length;i++){
            filterWords.add(args[i]);
        }
        DataBase.getInstance().filterWords(filterWords);
        // start server
        Server.reactor(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                () -> new BidiMessagingProtocolImpl(),
                () -> new MessageEncoderDecoderImpl()
        ).serve();

    }
}
