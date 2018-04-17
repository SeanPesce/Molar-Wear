package mw.molarwear.data.classes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 *
 * @author Sean Pesce
 *
 * @see    MolarWearProject
 * @see    StdSerializer
 */

public class ProjectSerializer extends StdSerializer<MolarWearProject> {

    public ProjectSerializer() {
        this(null);
    }

    public ProjectSerializer(Class<MolarWearProject> type) {
        super(type);
    }

    @Override
    public void serialize(MolarWearProject value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("title", value.title());
        //gen.writeBooleanField("saved", value.isSaved());
        gen.writeArrayFieldStart("subjects");
        for (int i = 0; i < value.subjectCount(); i++) {
            final MolarWearSubject subj = value.getSubject(i);
            gen.writeStartObject();
            gen.writeStringField("id", subj.id());
            gen.writeStringField("siteId", subj.siteId());
            gen.writeStringField("groupId", subj.groupId());
            gen.writeNumberField("age", subj.age());
            switch(subj.sex()) {
                case MALE:
                    gen.writeStringField("sex", "M");
                    break;
                case MALE_UNCONFIRMED:
                    gen.writeStringField("sex", "MU");
                    break;
                case FEMALE:
                    gen.writeStringField("sex", "F");
                    break;
                case FEMALE_UNCONFIRMED:
                    gen.writeStringField("sex", "FU");
                    break;
                case UNKNOWN:
                default:
                    gen.writeStringField("sex", "U");
                    break;
            }
            gen.writeStringField("notes", subj.notes());

            gen.writeFieldName("L1");
            gen.writeStartObject();
            gen.writeNumberField("Q1", subj.L1().surface().q1().wearScore());
            gen.writeNumberField("Q2", subj.L1().surface().q2().wearScore());
            gen.writeNumberField("Q3", subj.L1().surface().q3().wearScore());
            gen.writeNumberField("Q4", subj.L1().surface().q4().wearScore());
            gen.writeStringField("notes", subj.L1().notes());
            gen.writeEndObject();

            gen.writeFieldName("L2");
            gen.writeStartObject();
            gen.writeNumberField("Q1", subj.L2().surface().q1().wearScore());
            gen.writeNumberField("Q2", subj.L2().surface().q2().wearScore());
            gen.writeNumberField("Q3", subj.L2().surface().q3().wearScore());
            gen.writeNumberField("Q4", subj.L2().surface().q4().wearScore());
            gen.writeStringField("notes", subj.L2().notes());
            gen.writeEndObject();

            gen.writeFieldName("L3");
            gen.writeStartObject();
            gen.writeNumberField("Q1", subj.L3().surface().q1().wearScore());
            gen.writeNumberField("Q2", subj.L3().surface().q2().wearScore());
            gen.writeNumberField("Q3", subj.L3().surface().q3().wearScore());
            gen.writeNumberField("Q4", subj.L3().surface().q4().wearScore());
            gen.writeStringField("notes", subj.L3().notes());
            gen.writeEndObject();

            gen.writeFieldName("R1");
            gen.writeStartObject();
            gen.writeNumberField("Q1", subj.R1().surface().q1().wearScore());
            gen.writeNumberField("Q2", subj.R1().surface().q2().wearScore());
            gen.writeNumberField("Q3", subj.R1().surface().q3().wearScore());
            gen.writeNumberField("Q4", subj.R1().surface().q4().wearScore());
            gen.writeStringField("notes", subj.R1().notes());
            gen.writeEndObject();

            gen.writeFieldName("R2");
            gen.writeStartObject();
            gen.writeNumberField("Q1", subj.R2().surface().q1().wearScore());
            gen.writeNumberField("Q2", subj.R2().surface().q2().wearScore());
            gen.writeNumberField("Q3", subj.R2().surface().q3().wearScore());
            gen.writeNumberField("Q4", subj.R2().surface().q4().wearScore());
            gen.writeStringField("notes", subj.R2().notes());
            gen.writeEndObject();

            gen.writeFieldName("R3");
            gen.writeStartObject();
            gen.writeNumberField("Q1", subj.R3().surface().q1().wearScore());
            gen.writeNumberField("Q2", subj.R3().surface().q2().wearScore());
            gen.writeNumberField("Q3", subj.R3().surface().q3().wearScore());
            gen.writeNumberField("Q4", subj.R3().surface().q4().wearScore());
            gen.writeStringField("notes", subj.R3().notes());
            gen.writeEndObject();

            gen.writeEndObject(); // subject
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
