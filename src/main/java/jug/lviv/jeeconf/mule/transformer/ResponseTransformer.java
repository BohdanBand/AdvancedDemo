package jug.lviv.jeeconf.mule.transformer;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

public class ResponseTransformer extends AbstractMessageTransformer {

    @Override
    public Object transformMessage(MuleMessage muleMessage, String s) throws TransformerException {

        String message = StringUtils.EMPTY;
        if (muleMessage.getExceptionPayload() == null) {
            message = "File with content: <strong>" + muleMessage.getPayload() + "</strong> has been saved";
        } else {
            message = "Save file error:<br/>" + muleMessage.getExceptionPayload().getMessage();
        }


        muleMessage.setPayload(message);

        muleMessage.setProperty("Content-Type", "text/html", PropertyScope.OUTBOUND);

        return muleMessage;
    }
}
