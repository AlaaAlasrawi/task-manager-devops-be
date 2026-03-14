package api.todolist.demo.application.rest.specs;


import api.todolist.demo.persistence.entity.TaskEntity;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(params = "title", path = "title", spec = LikeIgnoreCase.class),
        @Spec(params = "createdAt.from", path = "createdAt", spec = GreaterThanOrEqual.class),
        @Spec(params = "createdAt.to", path = "createdAt", spec = LessThanOrEqual.class),
        @Spec(params = "isCompleted", path = "isCompleted", spec = Equal.class),
        @Spec(params = "appUser", path = "appUser.id", spec = Equal.class),
})
@Or({
        @Spec(params = "text", path = "title", spec = LikeIgnoreCase.class),
})
public interface TaskSpecs extends Specification<TaskEntity> {

}
