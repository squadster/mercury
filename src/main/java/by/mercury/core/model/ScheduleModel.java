package by.mercury.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity
@Table(name = "timetables")
public class ScheduleModel {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<LessonModel> lessons;
    
    @Column(name = "squad_id")
    private Long squad;
    
    private LocalDate date;
}
