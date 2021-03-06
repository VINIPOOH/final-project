package ua.training.cruise.entity.port;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.training.cruise.entity.cruise.Cruise;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Entity
@Table(name = "ports",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"port_name"})})

public class Port {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private long id;
    @Column(name = "port_name", nullable = false)
    private String portName;

    @OneToMany(mappedBy = "port")
    private List<Excursion> excursions;


    @ManyToMany(mappedBy = "ports", fetch = FetchType.LAZY)
    private List<Cruise> cruises;

    @Override
    public String toString() {
        return "Port{" +
                "id=" + id +
                ", portName='" + portName + '\'' +
                '}';
    }
}
