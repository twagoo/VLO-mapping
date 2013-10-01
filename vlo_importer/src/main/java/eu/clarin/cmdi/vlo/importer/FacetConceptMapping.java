package eu.clarin.cmdi.vlo.importer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Corresponds to the facetConcepts.xml file.
 * This class holds the mapping of facet name -> facetConcepts/patterns
 * A facetConcept is a ISOcat conceptLink e.g.: http://www.isocat.org/datcat/DC-2544
 * the conceptLink will be analysed and translated into a valid Xpath expression to extract data out of the metadata. 
 * Valid xpath expression e.g. /c:CMD/c:Header/c:MdSelfLink/text(), the 'c' namespace will be mapped to http://www.clarin.eu/cmd/ in the parser.
 * A pattern is an xpath expression used directly on the metadata. Use patterns only when a conceptLink does not suffice. 
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "facetConcepts")
public class FacetConceptMapping {

    private final static Logger LOG = LoggerFactory.getLogger(FacetConceptMapping.class);

    @XmlElement(name = "facetConcept")
    private List<FacetConcept> facetConcepts;

    public List<FacetConcept> getFacetConcepts() {
        return facetConcepts;
    }

    public void setFacetConcepts(List<FacetConcept> facetConcepts) {
        this.facetConcepts = facetConcepts;
    }
    
    public void check() {
        for (FacetConcept facetConcept : getFacetConcepts()) {
            if (facetConcept.hasAcceptableContext() && facetConcept.hasRejectableContext()) {
                AcceptableContext acceptableContext = facetConcept.getAcceptableContext();
                RejectableContext rejectableContext = facetConcept.getRejectableContext();
                if (acceptableContext.includeAny() && rejectableContext.includeAny())
                    LOG.error("Error: any context is both acceptable and rejectable for facet '"+facetConcept.getName()+"'");
                if (acceptableContext.includeEmpty() && rejectableContext.includeEmpty())
                    LOG.error("Error: empty context is both acceptable and rejectable for facet '"+facetConcept.getName()+"'");
            }
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "facetConcept")
    public static class FacetConcept {
        @XmlAttribute
        private String name;

        /**
         * Values will be stored lowercase by default, set isCaseInsensitive to true if you want to keep the case of the value
         */
        @XmlAttribute
        private boolean isCaseInsensitive = false;

        /**
         * By default multiple values that are found for a matching pattern will be stored. For some facets this leads to too much values
         * with little value for instance for "subject". Set allowMultipleValues to false will only store the first found value.
         */
        @XmlAttribute
        private boolean allowMultipleValues = true;

        @XmlElement(name = "concept")
        private List<String> concepts = new ArrayList<String>();

        @XmlElement(name = "acceptableContext")
        private AcceptableContext acceptableContext;

        @XmlElement(name = "rejectableContext")
        private RejectableContext rejectableContext;

        @XmlElement(name = "pattern")
        private List<String> patterns = new ArrayList<String>();
        
        public void setConcepts(List<String> concepts) {
            this.concepts = concepts;
        }

        public List<String> getConcepts() {
            return concepts;
        }

        public void setAccebtableContext(AcceptableContext context) {
            this.acceptableContext = context;
        }

        public AcceptableContext getAcceptableContext() {
            return acceptableContext;
        }
        
        public boolean hasAcceptableContext() {
            return (acceptableContext!=null);
        }

        public void setRejectableContext(RejectableContext context) {
            this.rejectableContext = context;
        }

        public RejectableContext getRejectableContext() {
            return rejectableContext;
        }
        
        public boolean hasRejectableContext() {
            return (rejectableContext!=null);
        }
        
        public boolean hasContext() {
            return (hasAcceptableContext() || hasRejectableContext());
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setCaseInsensitive(boolean isCaseInsensitive) {
            this.isCaseInsensitive = isCaseInsensitive;
        }

        public boolean isCaseInsensitive() {
            return isCaseInsensitive;
        }

        public void setAllowMultipleValues(boolean allowMultipleValues) {
            this.allowMultipleValues = allowMultipleValues;
        }

        public boolean isAllowMultipleValues() {
            return allowMultipleValues;
        }

        public void setPatterns(List<String> patterns) {
            this.patterns = patterns;
        }

        public List<String> getPatterns() {
            return patterns;
        }

        @Override
        public String toString() {
            return "name=" + name + ", patterns=" + patterns + ", concepts=" + concepts;
        }

    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "acceptableContext")
    public static class AcceptableContext {

        @XmlAttribute
        private boolean includeAny = false;

        @XmlAttribute
        private boolean includeEmpty = true;

        @XmlElement(name = "concept")
        private List<String> concepts = new ArrayList<String>();

        public void setConcepts(List<String> concepts) {
            this.concepts = concepts;
        }

        public List<String> getConcepts() {
            return concepts;
        }

        public void setIncludeAny(boolean includeAny) {
            this.includeAny = includeAny;
        }

        public boolean includeAny() {
            return includeAny;
        }

        public void setIncludeEmpty(boolean includeEmpty) {
            this.includeEmpty = includeEmpty;
        }

        public boolean includeEmpty() {
            return includeEmpty;
        }
        
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "rejectableContext")
    public static class RejectableContext {

        @XmlAttribute
        private boolean includeAny = true;

        @XmlAttribute
        private boolean includeEmpty = false;

        @XmlElement(name = "concept")
        private List<String> concepts = new ArrayList<String>();

        public void setConcepts(List<String> concepts) {
            this.concepts = concepts;
        }

        public List<String> getConcepts() {
            return concepts;
        }

        public void setIncludeAny(boolean includeAny) {
            this.includeAny = includeAny;
        }

        public boolean includeAny() {
            return includeAny;
        }

        public void setIncludeEmpty(boolean includeEmpty) {
            this.includeEmpty = includeEmpty;
        }

        public boolean includeEmpty() {
            return includeEmpty;
        }
        
    }

}
