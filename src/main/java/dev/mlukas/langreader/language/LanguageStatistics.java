package dev.mlukas.langreader.language;

import com.google.common.base.MoreObjects;

public class LanguageStatistics {
    private Language language;
    private int knownCount;
    private int studiedCount;
    private int ignoredCount;

    public LanguageStatistics() {
    }

    public LanguageStatistics(Language language, int knownCount, int studiedCount, int ignoredCount) {
        this.language = language;
        this.knownCount = knownCount;
        this.studiedCount = studiedCount;
        this.ignoredCount = ignoredCount;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public int getKnownCount() {
        return knownCount;
    }

    public void setKnownCount(int knownCount) {
        this.knownCount = knownCount;
    }

    public int getStudiedCount() {
        return studiedCount;
    }

    public void setStudiedCount(int studiedCount) {
        this.studiedCount = studiedCount;
    }

    public int getIgnoredCount() {
        return ignoredCount;
    }

    public void setIgnoredCount(int ignoredCount) {
        this.ignoredCount = ignoredCount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("language", language)
                .add("knownCount", knownCount)
                .add("studiedCount", studiedCount)
                .add("ignoredCount", ignoredCount)
                .toString();
    }
}
