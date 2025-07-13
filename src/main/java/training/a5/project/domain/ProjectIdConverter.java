package training.a5.project.domain;

import org.springframework.stereotype.Component;
import training.GenericIdConverter;

@Component
public class ProjectIdConverter extends GenericIdConverter<ProjectId> {
    public ProjectIdConverter() {
        super(ProjectId::new);
    }
}
