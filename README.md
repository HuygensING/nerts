# Named Entity Recognition Tool Set (Nerts)

Nerts is a collection of tools for rule-based Named Entity Recognition (NER).
It was developed for working with historical, multilingual texts.


## Basic operation

When you have instantiated and configured a `TeiAnnotator` instance `annotator`,
you can annotate an `xml` text by calling
```
annotatedXml = annotator.apply(xml);
```

Gazetteers can be obtained from CSV files with lines as
```
John;persName;id-john;7
```
that is, a name to be matched (John), the type of this name (persName), a unique identifier for that name (id-john), and an optional frequency count (7). N.B. If no unique identifier is available, a "?" may be used.

Given such a file, say "named-entities.txt", a `Gazetteer` instance may be created, and this gazetteer may be used in a `TeiAnnotator`:
```
NamedEntities entities = new NamedEntities().readFromFile("named-entities.txt");
Gazetteer gazetteer = entities.buildGazetteer();
TeiAnnotator annotator = new TeiAnnotator().withGazetteer(gazetteer);
```
Now, when applying the annotator to the text
```
<text>Hello, my name is John!</text>
```
you will get
```
<text>Hello, my name is <persName key="id-john">John</persName>!</text>
```


## Gazetteers

Gazetteers may contain various entities types, e.g. `persName`, `placeName`, `orgName`, if you are working with TEI documents, but you could also use `PER`, `LOC`, `ORG`, etc.

Nerts was developed when working with historical, multilingual texts. It internally uses a mapped version of the text you wish to annotate and allows you to control this mapping. By default Nerts uses three (combined) filters:
* `DiacriticsFilter` which removes accents (diacritical marks) from letters;
* `NameLinkFilter` which maps words such as "L", "De", "Du", "De;", "Van", "Von" to lowercase;
* `NERTextNormalizer` which maps characters to deal with spelling variation, the most important ones being `{u,v,w} --> u` and `{i,j,y} --> i`.

You can configure a gazetteer to use any such mapping by the call
```
gazetteer.withTextNormalizer(normalizer);
```
where `normalizer` is a function that maps a string to another string.
Note, however, that because of the matching algorithm used by Nerts the mapping must preserve the length of the text to be annotated. This is actually checked before the text is annotated; if the length is not preserved Nerts will use the unmapped text, with loss of accuracy.


## Language dependency

You can configure Nerts to work with a language identifier, a class that implements the `LanguageIdentifier` interface
```
annotator.withLanguageIdentifier(identifier);
```
If such an identifier is presented an intermediate version of the xml to be annotated will be used that contains the language of the text in TEI elements indicated (using a temporary attribute `LANGUAGE`). Those attributes will be removed in the final, annotated version of the text.
The purpose of language annotation is to make it possible to present language dependent exception lists: lists of words that must not be annotated, even though they occur in the gazetteer.
For example, suppose your gazetteer contains the place name `Mons`, but you do not want this name to be used in French texts because you know it will contain (many) abbreviations `Mons.` for `Monsieur`.
You can configure the `TextAnnotator` component to be aware of this exception:
```
TextAnnotator textAnnotator = new TextAnnotator().withSpecialExceptions("fr", "Mons");
annotator.withTextAnnotator(textAnnotator);
```
Nerts will annotate `Mons` in Dutch texts, but it will not do so in French texts:
```
<text><placeName>Mons</placeName> is een plaats in België.</text>
<text>J'ai écrivé un lattere a Mons. Descartes'</text>
```


## Tips for usage

Nerts allows you not to specify a gazetteer. If you do so, a gazetteer will be build in a first pass over the document.
This is useful for texts that already contain some annotation, usually made by a human editor, but these have not yet been fully applied.
For such texts it seems useful to first do a run without a gazetteer and then with an "external" gazetteer, assuming that the annotations present are accurate.

== EOF ==
