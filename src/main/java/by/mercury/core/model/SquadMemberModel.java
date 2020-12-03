package by.mercury.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity
@Table(name = "squad_members")
public class SquadMemberModel {

    @Id
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "squad_id")
    private Long squadId;
}
