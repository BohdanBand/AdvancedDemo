package jug.lviv.transformer;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

import java.util.*;

public class ParseRequestTransformer extends AbstractMessageTransformer {

    public static final String SAVE_TO = "saveTo";
    public static final String FILE_NAME = "fileName";

    @Override
    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {

        try {
            System.out.println(muleMessage.getPayloadAsString());

            String payload = muleMessage.getPayloadAsString();

            StringTokenizer tokenizer = new StringTokenizer(payload, "/");

            List<String> list = new ArrayList<String>();
            while (tokenizer.hasMoreElements()) {
                list.add(tokenizer.nextToken());
            }

            String saveTo = list.get(0);
            String fileName = list.get(1);
            String content = list.get(2);

            muleMessage.setPayload(content);

            Map<String, Object> saveToMap = new HashMap<String, Object>();
            saveToMap.put(SAVE_TO, saveTo);
            muleMessage.addProperties(saveToMap, PropertyScope.SESSION);

            Map<String, Object> fileNameMap = new HashMap<String, Object>();
            fileNameMap.put(FILE_NAME, fileName);
            muleMessage.addProperties(fileNameMap, PropertyScope.SESSION);

            muleMessage.setPayload(content);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return muleMessage;
    }
}
