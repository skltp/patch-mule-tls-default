import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * Created by gorerk on 5/14/2018.
 */
public class TestProducerLogger extends AbstractMessageTransformer {
    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
    return message;
    }

}
