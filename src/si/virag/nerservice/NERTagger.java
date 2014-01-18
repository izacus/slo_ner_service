package si.virag.nerservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class NERTagger extends javax.servlet.http.HttpServlet
{
    private CRFClassifier<CoreLabel> classifier;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException
    {
        String modelPath = getServletContext().getRealPath("lib/slovenian.ser.gz");
        classifier = CRFClassifier.getClassifierNoExceptions(modelPath);
        mapper = new ObjectMapper();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JsonNode node = mapper.readTree(request.getInputStream());
        String text = node.path("text").textValue();

        ArrayNode result = mapper.createArrayNode();

        List<List<CoreLabel>> classifications = classifier.classify(text);
        for (List<CoreLabel> sentence : classifications)
            for (CoreLabel word : sentence)
            {
                ObjectNode wordNode = mapper.createObjectNode();
                wordNode.put("word", word.word());

                String tag = word.get(CoreAnnotations.AnswerAnnotation.class);
                if (!tag.equals("O"))
                    wordNode.put("tag", tag);
                else if (tag.equals("osebno"))
                    wordNode.put("tag", "PERSON");
                else if (tag.equals("zemljepisno"))
                    wordNode.put("tag", "LOCATION");
                else if (tag.equals("stvarno"))
                    wordNode.put("tag", "OTHER");

                result.add(wordNode);
            }

        mapper.writeValue(response.getOutputStream(), result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.getOutputStream().write("Use POST.".getBytes());
    }
}
