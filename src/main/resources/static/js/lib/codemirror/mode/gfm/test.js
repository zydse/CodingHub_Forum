// CodeMirror, copyright (c) by Marijn Haverbeke and others
// Distributed under an MIT license: http://codemirror.net/LICENSE

(function() {
  var mode = CodeMirror.getMode({tabSize: 4}, "gfm");
  function MT(name) { test.mode(name, mode, Array.prototype.slice.call(arguments, 1)); }
  var modeHighlightFormatting = CodeMirror.getMode({tabSize: 4}, {name: "gfm", highlightFormatting: true});
  function FT(name) { test.mode(name, modeHighlightFormatting, Array.prototype.slice.call(arguments, 1)); }

  FT("codeBackticks",
     "[title&formatting&formatting-code `][title foo][title&formatting&formatting-code `]");

  FT("doubleBackticks",
     "[title&formatting&formatting-code ``][title foo ` bar][title&formatting&formatting-code ``]");

  FT("codeBlock",
     "[title&formatting&formatting-code-block ```css]",
     "[tag foo]",
     "[title&formatting&formatting-code-block ```]");

  FT("taskList",
     "[variable-2&formatting&formatting-list&formatting-list-ul - ][meta&formatting&formatting-task [ ]]][variable-2  foo]",
     "[variable-2&formatting&formatting-list&formatting-list-ul - ][property&formatting&formatting-task [x]]][variable-2  foo]");

  FT("formatting_strikethrough",
     "[strikethrough&formatting&formatting-strikethrough ~~][strikethrough foo][strikethrough&formatting&formatting-strikethrough ~~]");

  FT("formatting_strikethrough",
     "foo [strikethrough&formatting&formatting-strikethrough ~~][strikethrough bar][strikethrough&formatting&formatting-strikethrough ~~]");

  MT("emInWordAsterisk",
     "foo[em *bar*]hello");

  MT("emInWordUnderscore",
     "foo_bar_hello");

  MT("emStrongUnderscore",
     "[strong __][em&strong _foo__][em _] bar");

  MT("fencedCodeBlocks",
     "[title ```]",
     "[title foo]",
     "",
     "[title ```]",
     "bar");

  MT("fencedCodeBlockModeSwitching",
     "[title ```javascript]",
     "[variable foo]",
     "",
     "[title ```]",
     "bar");

  MT("taskListAsterisk",
     "[variable-2 * []] foo]", // Invalid; must have space or x between []
     "[variable-2 * [ ]]bar]", // Invalid; must have space after ]
     "[variable-2 * [x]]hello]", // Invalid; must have space after ]
     "[variable-2 * ][meta [ ]]][variable-2  [world]]]", // Valid; tests reference style links
     "    [variable-3 * ][property [x]]][variable-3  foo]"); // Valid; can be nested

  MT("taskListPlus",
     "[variable-2 + []] foo]", // Invalid; must have space or x between []
     "[variable-2 + [ ]]bar]", // Invalid; must have space after ]
     "[variable-2 + [x]]hello]", // Invalid; must have space after ]
     "[variable-2 + ][meta [ ]]][variable-2  [world]]]", // Valid; tests reference style links
     "    [variable-3 + ][property [x]]][variable-3  foo]"); // Valid; can be nested

  MT("taskListDash",
     "[variable-2 - []] foo]", // Invalid; must have space or x between []
     "[variable-2 - [ ]]bar]", // Invalid; must have space after ]
     "[variable-2 - [x]]hello]", // Invalid; must have space after ]
     "[variable-2 - ][meta [ ]]][variable-2  [world]]]", // Valid; tests reference style links
     "    [variable-3 - ][property [x]]][variable-3  foo]"); // Valid; can be nested

  MT("taskListNumber",
     "[variable-2 1. []] foo]", // Invalid; must have space or x between []
     "[variable-2 2. [ ]]bar]", // Invalid; must have space after ]
     "[variable-2 3. [x]]hello]", // Invalid; must have space after ]
     "[variable-2 4. ][meta [ ]]][variable-2  [world]]]", // Valid; tests reference style links
     "    [variable-3 1. ][property [x]]][variable-3  foo]"); // Valid; can be nested

  MT("SHA",
     "foo [link be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2] bar");

  MT("SHAEmphasis",
     "[em *foo ][em&link be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2][em *]");

  MT("shortSHA",
     "foo [link be6a8cc] bar");

  MT("tooShortSHA",
     "foo be6a8c bar");

  MT("longSHA",
     "foo be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd22 bar");

  MT("badSHA",
     "foo be6a8cc1c1ecfe9489fb51e4869af15a13fc2cg2 bar");

  MT("userSHA",
     "foo [link bar@be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2] hello");

  MT("userSHAEmphasis",
     "[em *foo ][em&link bar@be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2][em *]");

  MT("userProjectSHA",
     "foo [link bar/hello@be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2] world");

  MT("userProjectSHAEmphasis",
     "[em *foo ][em&link bar/hello@be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2][em *]");

  MT("num",
     "foo [link #1] bar");

  MT("numEmphasis",
     "[em *foo ][em&link #1][em *]");

  MT("badNum",
     "foo #1bar hello");

  MT("userNum",
     "foo [link bar#1] hello");

  MT("userNumEmphasis",
     "[em *foo ][em&link bar#1][em *]");

  MT("userProjectNum",
     "foo [link bar/hello#1] world");

  MT("userProjectNumEmphasis",
     "[em *foo ][em&link bar/hello#1][em *]");

  MT("vanillaLink",
     "foo [link http://www.example.com/] bar");

  MT("vanillaLinkPunctuation",
     "foo [link http://www.example.com/]. bar");

  MT("vanillaLinkExtension",
     "foo [link http://www.example.com/index.html] bar");

  MT("vanillaLinkEmphasis",
     "foo [em *][em&link http://www.example.com/index.html][em *] bar");

  MT("notALink",
     "[title ```css]",
     "[tag foo] {[property color]:[keyword black];}",
     "[title ```][link http://www.example.com/]");

  MT("notALink",
     "[title ``foo `bar` http://www.example.com/``] hello");

  MT("notALink",
     "[title `foo]",
     "[link http://www.example.com/]",
     "[title `foo]",
     "",
     "[link http://www.example.com/]");

  MT("headerCodeBlockGithub",
     "[header&header-1 # heading]",
     "",
     "[title ```]",
     "[title code]",
     "[title ```]",
     "",
     "Commit: [link be6a8cc1c1ecfe9489fb51e4869af15a13fc2cd2]",
     "Issue: [link #1]",
     "Link: [link http://www.example.com/]");

  MT("strikethrough",
     "[strikethrough ~~foo~~]");

  MT("strikethroughWithStartingSpace",
     "~~ foo~~");

  MT("strikethroughUnclosedStrayTildes",
    "[strikethrough ~~foo~~~]");

  MT("strikethroughUnclosedStrayTildes",
     "[strikethrough ~~foo ~~]");

  MT("strikethroughUnclosedStrayTildes",
    "[strikethrough ~~foo ~~ bar]");

  MT("strikethroughUnclosedStrayTildes",
    "[strikethrough ~~foo ~~ bar~~]hello");

  MT("strikethroughOneLetter",
     "[strikethrough ~~a~~]");

  MT("strikethroughWrapped",
     "[strikethrough ~~foo]",
     "[strikethrough foo~~]");

  MT("strikethroughParagraph",
     "[strikethrough ~~foo]",
     "",
     "foo[strikethrough ~~bar]");

  MT("strikethroughEm",
     "[strikethrough ~~foo][em&strikethrough *bar*][strikethrough ~~]");

  MT("strikethroughEm",
     "[em *][em&strikethrough ~~foo~~][em *]");

  MT("strikethroughStrong",
     "[strikethrough ~~][strong&strikethrough **foo**][strikethrough ~~]");

  MT("strikethroughStrong",
     "[strong **][strong&strikethrough ~~foo~~][strong **]");

})();
