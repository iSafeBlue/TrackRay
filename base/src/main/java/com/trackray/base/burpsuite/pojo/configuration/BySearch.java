package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class BySearch {

    @JsonProperty("case_sensitive")
    private boolean caseSensitive;
    @JsonProperty("negative_search")
    private boolean negativeSearch;
    private boolean regex;
    private String term;
    public void setCaseSensitive(boolean caseSensitive) {
         this.caseSensitive = caseSensitive;
     }
     public boolean getCaseSensitive() {
         return caseSensitive;
     }

    public void setNegativeSearch(boolean negativeSearch) {
         this.negativeSearch = negativeSearch;
     }
     public boolean getNegativeSearch() {
         return negativeSearch;
     }

    public void setRegex(boolean regex) {
         this.regex = regex;
     }
     public boolean getRegex() {
         return regex;
     }

    public void setTerm(String term) {
         this.term = term;
     }
     public String getTerm() {
         return term;
     }

}