package nl.knaw.huygens.pergamon.nerts.matcher;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.Trie.TrieBuilder;

import com.google.common.collect.Lists;

/**
 * DictionaryMatcher that uses the Aho-Corasick algorithm.
 * See <a href="https://github.com/robert-bor/aho-corasick">github</a>.
 */
class ACDictionaryMatcher implements Matcher {

  private final Trie trie;

  public ACDictionaryMatcher(Set<String> names) {
    Objects.requireNonNull(names);
    trie = buildTrie(names);
  }

  private Trie buildTrie(Set<String> names) {
    TrieBuilder builder = Trie.builder() //
        .onlyWholeWords() //
        .removeOverlaps();
    names.forEach(name -> builder.addKeyword(name));
    return builder.build();
  }

  @Override
  public List<Match> match(String text) {
    List<Match> result = Lists.newArrayList();
    trie.parseText(text) //
        .forEach(emit -> result.add(new Match(emit.getStart(), emit.getEnd() + 1)));
    return result;
  }

}
