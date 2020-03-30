//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.data.elasticsearch.core;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.piesat.monitor.entity.ssh.SshEntity;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

public class DefaultEntityMapper implements EntityMapper {
    private ObjectMapper objectMapper=new ObjectMapper();
    ;

    public DefaultEntityMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> context) {
        Assert.notNull(context, "MappingContext must not be null!");
        this.objectMapper = new ObjectMapper();
        //this.objectMapper.registerModule(new DefaultEntityMapper.SpringDataElasticsearchModule(context));
        this.objectMapper.registerModule(new CustomGeoModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    public String mapToString(Object object) throws IOException {
        String re= this.objectMapper.writeValueAsString(object);
        System.out.println(re);
        return re;
    }

    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return this.objectMapper.readValue(source, clazz);
    }

    private static class SpringDataElasticsearchModule extends SimpleModule {
        private static final long serialVersionUID = -9168968092458058966L;

        public SpringDataElasticsearchModule(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> context) {
            Assert.notNull(context, "MappingContext must not be null!");
            this.setSerializerModifier(new DefaultEntityMapper.SpringDataElasticsearchModule.SpringDataSerializerModifier(context));
        }

        private static class SpringDataSerializerModifier extends BeanSerializerModifier {
            private final MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> context;

            public SpringDataSerializerModifier(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> context) {
                Assert.notNull(context, "MappingContext must not be null!");
                this.context = context;
            }

            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription description, List<BeanPropertyWriter> properties) {
                Class<?> type = description.getBeanClass();
                ElasticsearchPersistentEntity<?> entity = (ElasticsearchPersistentEntity)this.context.getPersistentEntity(type);
                if(entity == null) {
                    return super.changeProperties(config, description, properties);
                } else {
                    List<BeanPropertyWriter> result = new ArrayList(properties.size());
                    Iterator var7 = properties.iterator();

                    while(var7.hasNext()) {
                        BeanPropertyWriter beanPropertyWriter = (BeanPropertyWriter)var7.next();
                        ElasticsearchPersistentProperty property = (ElasticsearchPersistentProperty)entity.getPersistentProperty(beanPropertyWriter.getName());
                        if(property != null && property.isWritable()) {
                            result.add(beanPropertyWriter);
                        }
                    }

                    return result;
                }
            }
        }
    }
}
