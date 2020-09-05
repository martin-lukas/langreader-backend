package net.langreader.model;

import lombok.*;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class LangStatistics {
    private Language language;
    private int knownCount;
    private int studiedCount;
    private int ignoredCount;
}
