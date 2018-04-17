package mw.molarwear.data.classes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

import mw.molarwear.data.classes.dental.molar.Surface;

/**
 *
 * @author Sean Pesce
 *
 * @see    MolarWearProject
 * @see    StdDeserializer
 */

public class ProjectDeserializer extends StdDeserializer<MolarWearProject> {

    public ProjectDeserializer() {
        this(null);
    }

    public ProjectDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    @Nullable
    public MolarWearProject deserialize(@NonNull JsonParser parser, @NonNull DeserializationContext context) {
        final JsonNode projectNode;
        try {
            projectNode = ((JsonNode) parser.getCodec().readTree(parser));
        } catch (IOException e) {
            return null;
        }
        final ArrayNode subjects = (ArrayNode) projectNode.get("subjects");
        final MolarWearProject project = new MolarWearProject();

        project.setTitle(projectNode.get("title").asText());
        //project.setSaved(projectNode.get("saved").asBoolean());

        for (int i = 0; i < subjects.size(); i++) {
            final MolarWearSubject s = new MolarWearSubject();
            final JsonNode L1 = subjects.get(i).get("L1"),
                           L2 = subjects.get(i).get("L2"),
                           L3 = subjects.get(i).get("L3"),
                           R1 = subjects.get(i).get("R1"),
                           R2 = subjects.get(i).get("R2"),
                           R3 = subjects.get(i).get("R3");
            s.setId(subjects.get(i).get("id").asText());
            s.setSiteId(subjects.get(i).get("siteId").asText());
            s.setGroupId(subjects.get(i).get("groupId").asText());
            s.setAge(subjects.get(i).get("age").asInt());
            switch (subjects.get(i).get("sex").asText()) {
                case "M":
                    s.setSex(MolarWearSubject.SEX.MALE);
                    break;
                case "MU":
                    s.setSex(MolarWearSubject.SEX.MALE_UNCONFIRMED);
                    break;
                case "F":
                    s.setSex(MolarWearSubject.SEX.FEMALE);
                    break;
                case "FU":
                    s.setSex(MolarWearSubject.SEX.FEMALE_UNCONFIRMED);
                    break;
                default:
                    s.setSex(MolarWearSubject.SEX.UNKNOWN);
                    break;
            }
            s.setL1(new Surface(L1.get("Q1").asInt(), L1.get("Q2").asInt(), L1.get("Q3").asInt(), L1.get("Q4").asInt()));
            s.setL2(new Surface(L2.get("Q1").asInt(), L2.get("Q2").asInt(), L2.get("Q3").asInt(), L2.get("Q4").asInt()));
            s.setL3(new Surface(L3.get("Q1").asInt(), L3.get("Q2").asInt(), L3.get("Q3").asInt(), L3.get("Q4").asInt()));
            s.setR1(new Surface(R1.get("Q1").asInt(), R1.get("Q2").asInt(), R1.get("Q3").asInt(), R1.get("Q4").asInt()));
            s.setR2(new Surface(R2.get("Q1").asInt(), R2.get("Q2").asInt(), R2.get("Q3").asInt(), R2.get("Q4").asInt()));
            s.setR3(new Surface(R3.get("Q1").asInt(), R3.get("Q2").asInt(), R3.get("Q3").asInt(), R3.get("Q4").asInt()));
            s.L1().setNotes(L1.get("notes").asText());
            s.L2().setNotes(L2.get("notes").asText());
            s.L3().setNotes(L3.get("notes").asText());
            s.R1().setNotes(R1.get("notes").asText());
            s.R2().setNotes(R2.get("notes").asText());
            s.R3().setNotes(R3.get("notes").asText());
            project.addSubject(s);
        }
        return project;
    }
}
