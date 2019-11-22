# Esperanto-Analiziloj

Esperanto-Analiziloj consists of two analyzation tools for the language Esperanto: A tool to analyze individual words and a tool to analyze the structure of a single sentence.

Esperanto uses the word-building grammar from germanic languages. That means that words can be made by sticking other words together, by attaching prefixes and suffixes, and adding endings. The word analyzer tries to find all plausible ways as what individual components constitute a given word. Moreover, it will determine some properties of the word, for instance whether it is a substantive, has the accusative case, is plural, or in case of a verb determine the transitivity.

The sentence analyzer makes an effort to parse a typed sentence and provide a basic grammatical analysis. It is currently limited to sentences that have only one clause (so one predicate or main verb). It does not support any kind of subordinate clauses.