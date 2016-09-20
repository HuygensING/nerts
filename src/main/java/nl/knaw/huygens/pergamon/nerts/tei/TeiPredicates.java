package nl.knaw.huygens.pergamon.nerts.tei;

import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Sets;

import nl.knaw.huygens.tei.Element;

public class TeiPredicates {

  private static final Set<String> NAMES = Sets.newHashSet("geogName", "name", "orgName", "persName", "placeName", "roleName", "rs");

  private static final Set<String> BLOCKED = Sets.newHashSet("NAME", "date", "figure", "formula", "note", "w");

  public static final Predicate<Element> isName = element -> NAMES.contains(element.getName());

  public static final Predicate<Element> isBlocked = element -> BLOCKED.contains(element.getName());

  public static final Predicate<Element> isProvenance = element -> element.hasName("div") && element.hasType("provenance");

  public static final Predicate<Element> isEditorial = element -> element.hasName("head") && element.hasType("ed");

  public static final Predicate<Element> isSpecial = isName.or(isBlocked).or(isProvenance).or(isEditorial);

  public static final Predicate<Element> isAnnotated = isSpecial.negate();

}
