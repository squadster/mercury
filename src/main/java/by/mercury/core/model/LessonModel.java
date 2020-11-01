package by.mercury.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@Entity
@Table(name = "lessons")
public class LessonModel {

    @Id
    @Column(name = "id")
    private Long id;
    
    private String name;
    
    private String teacher;
    
    private Integer index;
    
    private String note;
    
    @ManyToOne
    @JoinColumn(name = "timetable_id")
    private ScheduleModel schedule;
}
