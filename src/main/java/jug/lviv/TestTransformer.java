package jug.lviv;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class TestTransformer extends AbstractMessageTransformer {

    @Override
    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {
        try {
            System.out.println(muleMessage.getPayloadAsString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return muleMessage;
    }
}
