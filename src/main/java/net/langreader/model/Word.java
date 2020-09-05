package net.langreader.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "words")
@Getter @Setter @NoArgsConstructor @ToString
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    @Enumerated
    @Column(columnDefinition = "int", name = "word_type_id")
    private WordType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lang_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
