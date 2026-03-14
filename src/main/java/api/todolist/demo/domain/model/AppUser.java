package api.todolist.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    private Long id;

    private String username;

    private String password;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
