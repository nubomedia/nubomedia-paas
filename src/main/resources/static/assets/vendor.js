(function( global, factory ) {

  if ( typeof module === "object" && typeof module.exports === "object" ) {
    // For CommonJS and CommonJS-like environments where a proper `window`
    // is present, execute the factory and get jQuery.
    // For environments that do not have a `window` with a `document`
    // (such as Node.js), expose a factory as module.exports.
    // This accentuates the need for the creation of a real `window`.
    // e.g. var jQuery = require("jquery")(window);
    // See ticket #14549 for more info.
    module.exports = global.document ?
      factory( global, true ) :
      function( w ) {
        if ( !w.document ) {
          throw new Error( "jQuery requires a window with a document" );
        }
        return factory( w );
      };
  } else {
    factory( global );
  }

// Pass this if window is not defined yet
}(typeof window !== "undefined" ? window : this, function( window, noGlobal ) {

// Support: Firefox 18+
// Can't be in strict mode, several libs including ASP.NET trace
// the stack via arguments.caller.callee and Firefox dies if
// you try to trace through "use strict" call chains. (#13335)
//"use strict";
var arr = [];

var document = window.document;

var slice = arr.slice;

var concat = arr.concat;

var push = arr.push;

var indexOf = arr.indexOf;

var class2type = {};

var toString = class2type.toString;

var hasOwn = class2type.hasOwnProperty;

var support = {};



var
  version = "2.2.4",

  // Define a local copy of jQuery
  jQuery = function( selector, context ) {

    // The jQuery object is actually just the init constructor 'enhanced'
    // Need init if jQuery is called (just allow error to be thrown if not included)
    return new jQuery.fn.init( selector, context );
  },

  // Support: Android<4.1
  // Make sure we trim BOM and NBSP
  rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,

  // Matches dashed string for camelizing
  rmsPrefix = /^-ms-/,
  rdashAlpha = /-([\da-z])/gi,

  // Used by jQuery.camelCase as callback to replace()
  fcamelCase = function( all, letter ) {
    return letter.toUpperCase();
  };

jQuery.fn = jQuery.prototype = {

  // The current version of jQuery being used
  jquery: version,

  constructor: jQuery,

  // Start with an empty selector
  selector: "",

  // The default length of a jQuery object is 0
  length: 0,

  toArray: function() {
    return slice.call( this );
  },

  // Get the Nth element in the matched element set OR
  // Get the whole matched element set as a clean array
  get: function( num ) {
    return num != null ?

      // Return just the one element from the set
      ( num < 0 ? this[ num + this.length ] : this[ num ] ) :

      // Return all the elements in a clean array
      slice.call( this );
  },

  // Take an array of elements and push it onto the stack
  // (returning the new matched element set)
  pushStack: function( elems ) {

    // Build a new jQuery matched element set
    var ret = jQuery.merge( this.constructor(), elems );

    // Add the old object onto the stack (as a reference)
    ret.prevObject = this;
    ret.context = this.context;

    // Return the newly-formed element set
    return ret;
  },

  // Execute a callback for every element in the matched set.
  each: function( callback ) {
    return jQuery.each( this, callback );
  },

  map: function( callback ) {
    return this.pushStack( jQuery.map( this, function( elem, i ) {
      return callback.call( elem, i, elem );
    } ) );
  },

  slice: function() {
    return this.pushStack( slice.apply( this, arguments ) );
  },

  first: function() {
    return this.eq( 0 );
  },

  last: function() {
    return this.eq( -1 );
  },

  eq: function( i ) {
    var len = this.length,
      j = +i + ( i < 0 ? len : 0 );
    return this.pushStack( j >= 0 && j < len ? [ this[ j ] ] : [] );
  },

  end: function() {
    return this.prevObject || this.constructor();
  },

  // For internal use only.
  // Behaves like an Array's method, not like a jQuery method.
  push: push,
  sort: arr.sort,
  splice: arr.splice
};

jQuery.extend = jQuery.fn.extend = function() {
  var options, name, src, copy, copyIsArray, clone,
    target = arguments[ 0 ] || {},
    i = 1,
    length = arguments.length,
    deep = false;

  // Handle a deep copy situation
  if ( typeof target === "boolean" ) {
    deep = target;

    // Skip the boolean and the target
    target = arguments[ i ] || {};
    i++;
  }

  // Handle case when target is a string or something (possible in deep copy)
  if ( typeof target !== "object" && !jQuery.isFunction( target ) ) {
    target = {};
  }

  // Extend jQuery itself if only one argument is passed
  if ( i === length ) {
    target = this;
    i--;
  }

  for ( ; i < length; i++ ) {

    // Only deal with non-null/undefined values
    if ( ( options = arguments[ i ] ) != null ) {

      // Extend the base object
      for ( name in options ) {
        src = target[ name ];
        copy = options[ name ];

        // Prevent never-ending loop
        if ( target === copy ) {
          continue;
        }

        // Recurse if we're merging plain objects or arrays
        if ( deep && copy && ( jQuery.isPlainObject( copy ) ||
          ( copyIsArray = jQuery.isArray( copy ) ) ) ) {

          if ( copyIsArray ) {
            copyIsArray = false;
            clone = src && jQuery.isArray( src ) ? src : [];

          } else {
            clone = src && jQuery.isPlainObject( src ) ? src : {};
          }

          // Never move original objects, clone them
          target[ name ] = jQuery.extend( deep, clone, copy );

        // Don't bring in undefined values
        } else if ( copy !== undefined ) {
          target[ name ] = copy;
        }
      }
    }
  }

  // Return the modified object
  return target;
};

jQuery.extend( {

  // Unique for each copy of jQuery on the page
  expando: "jQuery" + ( version + Math.random() ).replace( /\D/g, "" ),

  // Assume jQuery is ready without the ready module
  isReady: true,

  error: function( msg ) {
    throw new Error( msg );
  },

  noop: function() {},

  isFunction: function( obj ) {
    return jQuery.type( obj ) === "function";
  },

  isArray: Array.isArray,

  isWindow: function( obj ) {
    return obj != null && obj === obj.window;
  },

  isNumeric: function( obj ) {

    // parseFloat NaNs numeric-cast false positives (null|true|false|"")
    // ...but misinterprets leading-number strings, particularly hex literals ("0x...")
    // subtraction forces infinities to NaN
    // adding 1 corrects loss of precision from parseFloat (#15100)
    var realStringObj = obj && obj.toString();
    return !jQuery.isArray( obj ) && ( realStringObj - parseFloat( realStringObj ) + 1 ) >= 0;
  },

  isPlainObject: function( obj ) {
    var key;

    // Not plain objects:
    // - Any object or value whose internal [[Class]] property is not "[object Object]"
    // - DOM nodes
    // - window
    if ( jQuery.type( obj ) !== "object" || obj.nodeType || jQuery.isWindow( obj ) ) {
      return false;
    }

    // Not own constructor property must be Object
    if ( obj.constructor &&
        !hasOwn.call( obj, "constructor" ) &&
        !hasOwn.call( obj.constructor.prototype || {}, "isPrototypeOf" ) ) {
      return false;
    }

    // Own properties are enumerated firstly, so to speed up,
    // if last one is own, then all properties are own
    for ( key in obj ) {}

    return key === undefined || hasOwn.call( obj, key );
  },

  isEmptyObject: function( obj ) {
    var name;
    for ( name in obj ) {
      return false;
    }
    return true;
  },

  type: function( obj ) {
    if ( obj == null ) {
      return obj + "";
    }

    // Support: Android<4.0, iOS<6 (functionish RegExp)
    return typeof obj === "object" || typeof obj === "function" ?
      class2type[ toString.call( obj ) ] || "object" :
      typeof obj;
  },

  // Evaluates a script in a global context
  globalEval: function( code ) {
    var script,
      indirect = eval;

    code = jQuery.trim( code );

    if ( code ) {

      // If the code includes a valid, prologue position
      // strict mode pragma, execute code by injecting a
      // script tag into the document.
      if ( code.indexOf( "use strict" ) === 1 ) {
        script = document.createElement( "script" );
        script.text = code;
        document.head.appendChild( script ).parentNode.removeChild( script );
      } else {

        // Otherwise, avoid the DOM node creation, insertion
        // and removal by using an indirect global eval

        indirect( code );
      }
    }
  },

  // Convert dashed to camelCase; used by the css and data modules
  // Support: IE9-11+
  // Microsoft forgot to hump their vendor prefix (#9572)
  camelCase: function( string ) {
    return string.replace( rmsPrefix, "ms-" ).replace( rdashAlpha, fcamelCase );
  },

  nodeName: function( elem, name ) {
    return elem.nodeName && elem.nodeName.toLowerCase() === name.toLowerCase();
  },

  each: function( obj, callback ) {
    var length, i = 0;

    if ( isArrayLike( obj ) ) {
      length = obj.length;
      for ( ; i < length; i++ ) {
        if ( callback.call( obj[ i ], i, obj[ i ] ) === false ) {
          break;
        }
      }
    } else {
      for ( i in obj ) {
        if ( callback.call( obj[ i ], i, obj[ i ] ) === false ) {
          break;
        }
      }
    }

    return obj;
  },

  // Support: Android<4.1
  trim: function( text ) {
    return text == null ?
      "" :
      ( text + "" ).replace( rtrim, "" );
  },

  // results is for internal usage only
  makeArray: function( arr, results ) {
    var ret = results || [];

    if ( arr != null ) {
      if ( isArrayLike( Object( arr ) ) ) {
        jQuery.merge( ret,
          typeof arr === "string" ?
          [ arr ] : arr
        );
      } else {
        push.call( ret, arr );
      }
    }

    return ret;
  },

  inArray: function( elem, arr, i ) {
    return arr == null ? -1 : indexOf.call( arr, elem, i );
  },

  merge: function( first, second ) {
    var len = +second.length,
      j = 0,
      i = first.length;

    for ( ; j < len; j++ ) {
      first[ i++ ] = second[ j ];
    }

    first.length = i;

    return first;
  },

  grep: function( elems, callback, invert ) {
    var callbackInverse,
      matches = [],
      i = 0,
      length = elems.length,
      callbackExpect = !invert;

    // Go through the array, only saving the items
    // that pass the validator function
    for ( ; i < length; i++ ) {
      callbackInverse = !callback( elems[ i ], i );
      if ( callbackInverse !== callbackExpect ) {
        matches.push( elems[ i ] );
      }
    }

    return matches;
  },

  // arg is for internal usage only
  map: function( elems, callback, arg ) {
    var length, value,
      i = 0,
      ret = [];

    // Go through the array, translating each of the items to their new values
    if ( isArrayLike( elems ) ) {
      length = elems.length;
      for ( ; i < length; i++ ) {
        value = callback( elems[ i ], i, arg );

        if ( value != null ) {
          ret.push( value );
        }
      }

    // Go through every key on the object,
    } else {
      for ( i in elems ) {
        value = callback( elems[ i ], i, arg );

        if ( value != null ) {
          ret.push( value );
        }
      }
    }

    // Flatten any nested arrays
    return concat.apply( [], ret );
  },

  // A global GUID counter for objects
  guid: 1,

  // Bind a function to a context, optionally partially applying any
  // arguments.
  proxy: function( fn, context ) {
    var tmp, args, proxy;

    if ( typeof context === "string" ) {
      tmp = fn[ context ];
      context = fn;
      fn = tmp;
    }

    // Quick check to determine if target is callable, in the spec
    // this throws a TypeError, but we will just return undefined.
    if ( !jQuery.isFunction( fn ) ) {
      return undefined;
    }

    // Simulated bind
    args = slice.call( arguments, 2 );
    proxy = function() {
      return fn.apply( context || this, args.concat( slice.call( arguments ) ) );
    };

    // Set the guid of unique handler to the same of original handler, so it can be removed
    proxy.guid = fn.guid = fn.guid || jQuery.guid++;

    return proxy;
  },

  now: Date.now,

  // jQuery.support is not used in Core but other projects attach their
  // properties to it so it needs to exist.
  support: support
} );

// JSHint would error on this code due to the Symbol not being defined in ES5.
// Defining this global in .jshintrc would create a danger of using the global
// unguarded in another place, it seems safer to just disable JSHint for these
// three lines.
/* jshint ignore: start */
if ( typeof Symbol === "function" ) {
  jQuery.fn[ Symbol.iterator ] = arr[ Symbol.iterator ];
}
/* jshint ignore: end */

// Populate the class2type map
jQuery.each( "Boolean Number String Function Array Date RegExp Object Error Symbol".split( " " ),
function( i, name ) {
  class2type[ "[object " + name + "]" ] = name.toLowerCase();
} );

function isArrayLike( obj ) {

  // Support: iOS 8.2 (not reproducible in simulator)
  // `in` check used to prevent JIT error (gh-2145)
  // hasOwn isn't used here due to false negatives
  // regarding Nodelist length in IE
  var length = !!obj && "length" in obj && obj.length,
    type = jQuery.type( obj );

  if ( type === "function" || jQuery.isWindow( obj ) ) {
    return false;
  }

  return type === "array" || length === 0 ||
    typeof length === "number" && length > 0 && ( length - 1 ) in obj;
}
var Sizzle =
/*!
 * Sizzle CSS Selector Engine v2.2.1
 * http://sizzlejs.com/
 *
 * Copyright jQuery Foundation and other contributors
 * Released under the MIT license
 * http://jquery.org/license
 *
 * Date: 2015-10-17
 */
(function( window ) {

var i,
  support,
  Expr,
  getText,
  isXML,
  tokenize,
  compile,
  select,
  outermostContext,
  sortInput,
  hasDuplicate,

  // Local document vars
  setDocument,
  document,
  docElem,
  documentIsHTML,
  rbuggyQSA,
  rbuggyMatches,
  matches,
  contains,

  // Instance-specific data
  expando = "sizzle" + 1 * new Date(),
  preferredDoc = window.document,
  dirruns = 0,
  done = 0,
  classCache = createCache(),
  tokenCache = createCache(),
  compilerCache = createCache(),
  sortOrder = function( a, b ) {
    if ( a === b ) {
      hasDuplicate = true;
    }
    return 0;
  },

  // General-purpose constants
  MAX_NEGATIVE = 1 << 31,

  // Instance methods
  hasOwn = ({}).hasOwnProperty,
  arr = [],
  pop = arr.pop,
  push_native = arr.push,
  push = arr.push,
  slice = arr.slice,
  // Use a stripped-down indexOf as it's faster than native
  // http://jsperf.com/thor-indexof-vs-for/5
  indexOf = function( list, elem ) {
    var i = 0,
      len = list.length;
    for ( ; i < len; i++ ) {
      if ( list[i] === elem ) {
        return i;
      }
    }
    return -1;
  },

  booleans = "checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped",

  // Regular expressions

  // http://www.w3.org/TR/css3-selectors/#whitespace
  whitespace = "[\\x20\\t\\r\\n\\f]",

  // http://www.w3.org/TR/CSS21/syndata.html#value-def-identifier
  identifier = "(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+",

  // Attribute selectors: http://www.w3.org/TR/selectors/#attribute-selectors
  attributes = "\\[" + whitespace + "*(" + identifier + ")(?:" + whitespace +
    // Operator (capture 2)
    "*([*^$|!~]?=)" + whitespace +
    // "Attribute values must be CSS identifiers [capture 5] or strings [capture 3 or capture 4]"
    "*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|(" + identifier + "))|)" + whitespace +
    "*\\]",

  pseudos = ":(" + identifier + ")(?:\\((" +
    // To reduce the number of selectors needing tokenize in the preFilter, prefer arguments:
    // 1. quoted (capture 3; capture 4 or capture 5)
    "('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|" +
    // 2. simple (capture 6)
    "((?:\\\\.|[^\\\\()[\\]]|" + attributes + ")*)|" +
    // 3. anything else (capture 2)
    ".*" +
    ")\\)|)",

  // Leading and non-escaped trailing whitespace, capturing some non-whitespace characters preceding the latter
  rwhitespace = new RegExp( whitespace + "+", "g" ),
  rtrim = new RegExp( "^" + whitespace + "+|((?:^|[^\\\\])(?:\\\\.)*)" + whitespace + "+$", "g" ),

  rcomma = new RegExp( "^" + whitespace + "*," + whitespace + "*" ),
  rcombinators = new RegExp( "^" + whitespace + "*([>+~]|" + whitespace + ")" + whitespace + "*" ),

  rattributeQuotes = new RegExp( "=" + whitespace + "*([^\\]'\"]*?)" + whitespace + "*\\]", "g" ),

  rpseudo = new RegExp( pseudos ),
  ridentifier = new RegExp( "^" + identifier + "$" ),

  matchExpr = {
    "ID": new RegExp( "^#(" + identifier + ")" ),
    "CLASS": new RegExp( "^\\.(" + identifier + ")" ),
    "TAG": new RegExp( "^(" + identifier + "|[*])" ),
    "ATTR": new RegExp( "^" + attributes ),
    "PSEUDO": new RegExp( "^" + pseudos ),
    "CHILD": new RegExp( "^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\(" + whitespace +
      "*(even|odd|(([+-]|)(\\d*)n|)" + whitespace + "*(?:([+-]|)" + whitespace +
      "*(\\d+)|))" + whitespace + "*\\)|)", "i" ),
    "bool": new RegExp( "^(?:" + booleans + ")$", "i" ),
    // For use in libraries implementing .is()
    // We use this for POS matching in `select`
    "needsContext": new RegExp( "^" + whitespace + "*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\(" +
      whitespace + "*((?:-\\d)?\\d*)" + whitespace + "*\\)|)(?=[^-]|$)", "i" )
  },

  rinputs = /^(?:input|select|textarea|button)$/i,
  rheader = /^h\d$/i,

  rnative = /^[^{]+\{\s*\[native \w/,

  // Easily-parseable/retrievable ID or TAG or CLASS selectors
  rquickExpr = /^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,

  rsibling = /[+~]/,
  rescape = /'|\\/g,

  // CSS escapes http://www.w3.org/TR/CSS21/syndata.html#escaped-characters
  runescape = new RegExp( "\\\\([\\da-f]{1,6}" + whitespace + "?|(" + whitespace + ")|.)", "ig" ),
  funescape = function( _, escaped, escapedWhitespace ) {
    var high = "0x" + escaped - 0x10000;
    // NaN means non-codepoint
    // Support: Firefox<24
    // Workaround erroneous numeric interpretation of +"0x"
    return high !== high || escapedWhitespace ?
      escaped :
      high < 0 ?
        // BMP codepoint
        String.fromCharCode( high + 0x10000 ) :
        // Supplemental Plane codepoint (surrogate pair)
        String.fromCharCode( high >> 10 | 0xD800, high & 0x3FF | 0xDC00 );
  },

  // Used for iframes
  // See setDocument()
  // Removing the function wrapper causes a "Permission Denied"
  // error in IE
  unloadHandler = function() {
    setDocument();
  };

// Optimize for push.apply( _, NodeList )
try {
  push.apply(
    (arr = slice.call( preferredDoc.childNodes )),
    preferredDoc.childNodes
  );
  // Support: Android<4.0
  // Detect silently failing push.apply
  arr[ preferredDoc.childNodes.length ].nodeType;
} catch ( e ) {
  push = { apply: arr.length ?

    // Leverage slice if possible
    function( target, els ) {
      push_native.apply( target, slice.call(els) );
    } :

    // Support: IE<9
    // Otherwise append directly
    function( target, els ) {
      var j = target.length,
        i = 0;
      // Can't trust NodeList.length
      while ( (target[j++] = els[i++]) ) {}
      target.length = j - 1;
    }
  };
}

function Sizzle( selector, context, results, seed ) {
  var m, i, elem, nid, nidselect, match, groups, newSelector,
    newContext = context && context.ownerDocument,

    // nodeType defaults to 9, since context defaults to document
    nodeType = context ? context.nodeType : 9;

  results = results || [];

  // Return early from calls with invalid selector or context
  if ( typeof selector !== "string" || !selector ||
    nodeType !== 1 && nodeType !== 9 && nodeType !== 11 ) {

    return results;
  }

  // Try to shortcut find operations (as opposed to filters) in HTML documents
  if ( !seed ) {

    if ( ( context ? context.ownerDocument || context : preferredDoc ) !== document ) {
      setDocument( context );
    }
    context = context || document;

    if ( documentIsHTML ) {

      // If the selector is sufficiently simple, try using a "get*By*" DOM method
      // (excepting DocumentFragment context, where the methods don't exist)
      if ( nodeType !== 11 && (match = rquickExpr.exec( selector )) ) {

        // ID selector
        if ( (m = match[1]) ) {

          // Document context
          if ( nodeType === 9 ) {
            if ( (elem = context.getElementById( m )) ) {

              // Support: IE, Opera, Webkit
              // TODO: identify versions
              // getElementById can match elements by name instead of ID
              if ( elem.id === m ) {
                results.push( elem );
                return results;
              }
            } else {
              return results;
            }

          // Element context
          } else {

            // Support: IE, Opera, Webkit
            // TODO: identify versions
            // getElementById can match elements by name instead of ID
            if ( newContext && (elem = newContext.getElementById( m )) &&
              contains( context, elem ) &&
              elem.id === m ) {

              results.push( elem );
              return results;
            }
          }

        // Type selector
        } else if ( match[2] ) {
          push.apply( results, context.getElementsByTagName( selector ) );
          return results;

        // Class selector
        } else if ( (m = match[3]) && support.getElementsByClassName &&
          context.getElementsByClassName ) {

          push.apply( results, context.getElementsByClassName( m ) );
          return results;
        }
      }

      // Take advantage of querySelectorAll
      if ( support.qsa &&
        !compilerCache[ selector + " " ] &&
        (!rbuggyQSA || !rbuggyQSA.test( selector )) ) {

        if ( nodeType !== 1 ) {
          newContext = context;
          newSelector = selector;

        // qSA looks outside Element context, which is not what we want
        // Thanks to Andrew Dupont for this workaround technique
        // Support: IE <=8
        // Exclude object elements
        } else if ( context.nodeName.toLowerCase() !== "object" ) {

          // Capture the context ID, setting it first if necessary
          if ( (nid = context.getAttribute( "id" )) ) {
            nid = nid.replace( rescape, "\\$&" );
          } else {
            context.setAttribute( "id", (nid = expando) );
          }

          // Prefix every selector in the list
          groups = tokenize( selector );
          i = groups.length;
          nidselect = ridentifier.test( nid ) ? "#" + nid : "[id='" + nid + "']";
          while ( i-- ) {
            groups[i] = nidselect + " " + toSelector( groups[i] );
          }
          newSelector = groups.join( "," );

          // Expand context for sibling selectors
          newContext = rsibling.test( selector ) && testContext( context.parentNode ) ||
            context;
        }

        if ( newSelector ) {
          try {
            push.apply( results,
              newContext.querySelectorAll( newSelector )
            );
            return results;
          } catch ( qsaError ) {
          } finally {
            if ( nid === expando ) {
              context.removeAttribute( "id" );
            }
          }
        }
      }
    }
  }

  // All others
  return select( selector.replace( rtrim, "$1" ), context, results, seed );
}

/**
 * Create key-value caches of limited size
 * @returns {function(string, object)} Returns the Object data after storing it on itself with
 *  property name the (space-suffixed) string and (if the cache is larger than Expr.cacheLength)
 *  deleting the oldest entry
 */
function createCache() {
  var keys = [];

  function cache( key, value ) {
    // Use (key + " ") to avoid collision with native prototype properties (see Issue #157)
    if ( keys.push( key + " " ) > Expr.cacheLength ) {
      // Only keep the most recent entries
      delete cache[ keys.shift() ];
    }
    return (cache[ key + " " ] = value);
  }
  return cache;
}

/**
 * Mark a function for special use by Sizzle
 * @param {Function} fn The function to mark
 */
function markFunction( fn ) {
  fn[ expando ] = true;
  return fn;
}

/**
 * Support testing using an element
 * @param {Function} fn Passed the created div and expects a boolean result
 */
function assert( fn ) {
  var div = document.createElement("div");

  try {
    return !!fn( div );
  } catch (e) {
    return false;
  } finally {
    // Remove from its parent by default
    if ( div.parentNode ) {
      div.parentNode.removeChild( div );
    }
    // release memory in IE
    div = null;
  }
}

/**
 * Adds the same handler for all of the specified attrs
 * @param {String} attrs Pipe-separated list of attributes
 * @param {Function} handler The method that will be applied
 */
function addHandle( attrs, handler ) {
  var arr = attrs.split("|"),
    i = arr.length;

  while ( i-- ) {
    Expr.attrHandle[ arr[i] ] = handler;
  }
}

/**
 * Checks document order of two siblings
 * @param {Element} a
 * @param {Element} b
 * @returns {Number} Returns less than 0 if a precedes b, greater than 0 if a follows b
 */
function siblingCheck( a, b ) {
  var cur = b && a,
    diff = cur && a.nodeType === 1 && b.nodeType === 1 &&
      ( ~b.sourceIndex || MAX_NEGATIVE ) -
      ( ~a.sourceIndex || MAX_NEGATIVE );

  // Use IE sourceIndex if available on both nodes
  if ( diff ) {
    return diff;
  }

  // Check if b follows a
  if ( cur ) {
    while ( (cur = cur.nextSibling) ) {
      if ( cur === b ) {
        return -1;
      }
    }
  }

  return a ? 1 : -1;
}

/**
 * Returns a function to use in pseudos for input types
 * @param {String} type
 */
function createInputPseudo( type ) {
  return function( elem ) {
    var name = elem.nodeName.toLowerCase();
    return name === "input" && elem.type === type;
  };
}

/**
 * Returns a function to use in pseudos for buttons
 * @param {String} type
 */
function createButtonPseudo( type ) {
  return function( elem ) {
    var name = elem.nodeName.toLowerCase();
    return (name === "input" || name === "button") && elem.type === type;
  };
}

/**
 * Returns a function to use in pseudos for positionals
 * @param {Function} fn
 */
function createPositionalPseudo( fn ) {
  return markFunction(function( argument ) {
    argument = +argument;
    return markFunction(function( seed, matches ) {
      var j,
        matchIndexes = fn( [], seed.length, argument ),
        i = matchIndexes.length;

      // Match elements found at the specified indexes
      while ( i-- ) {
        if ( seed[ (j = matchIndexes[i]) ] ) {
          seed[j] = !(matches[j] = seed[j]);
        }
      }
    });
  });
}

/**
 * Checks a node for validity as a Sizzle context
 * @param {Element|Object=} context
 * @returns {Element|Object|Boolean} The input node if acceptable, otherwise a falsy value
 */
function testContext( context ) {
  return context && typeof context.getElementsByTagName !== "undefined" && context;
}

// Expose support vars for convenience
support = Sizzle.support = {};

/**
 * Detects XML nodes
 * @param {Element|Object} elem An element or a document
 * @returns {Boolean} True iff elem is a non-HTML XML node
 */
isXML = Sizzle.isXML = function( elem ) {
  // documentElement is verified for cases where it doesn't yet exist
  // (such as loading iframes in IE - #4833)
  var documentElement = elem && (elem.ownerDocument || elem).documentElement;
  return documentElement ? documentElement.nodeName !== "HTML" : false;
};

/**
 * Sets document-related variables once based on the current document
 * @param {Element|Object} [doc] An element or document object to use to set the document
 * @returns {Object} Returns the current document
 */
setDocument = Sizzle.setDocument = function( node ) {
  var hasCompare, parent,
    doc = node ? node.ownerDocument || node : preferredDoc;

  // Return early if doc is invalid or already selected
  if ( doc === document || doc.nodeType !== 9 || !doc.documentElement ) {
    return document;
  }

  // Update global variables
  document = doc;
  docElem = document.documentElement;
  documentIsHTML = !isXML( document );

  // Support: IE 9-11, Edge
  // Accessing iframe documents after unload throws "permission denied" errors (jQuery #13936)
  if ( (parent = document.defaultView) && parent.top !== parent ) {
    // Support: IE 11
    if ( parent.addEventListener ) {
      parent.addEventListener( "unload", unloadHandler, false );

    // Support: IE 9 - 10 only
    } else if ( parent.attachEvent ) {
      parent.attachEvent( "onunload", unloadHandler );
    }
  }

  /* Attributes
  ---------------------------------------------------------------------- */

  // Support: IE<8
  // Verify that getAttribute really returns attributes and not properties
  // (excepting IE8 booleans)
  support.attributes = assert(function( div ) {
    div.className = "i";
    return !div.getAttribute("className");
  });

  /* getElement(s)By*
  ---------------------------------------------------------------------- */

  // Check if getElementsByTagName("*") returns only elements
  support.getElementsByTagName = assert(function( div ) {
    div.appendChild( document.createComment("") );
    return !div.getElementsByTagName("*").length;
  });

  // Support: IE<9
  support.getElementsByClassName = rnative.test( document.getElementsByClassName );

  // Support: IE<10
  // Check if getElementById returns elements by name
  // The broken getElementById methods don't pick up programatically-set names,
  // so use a roundabout getElementsByName test
  support.getById = assert(function( div ) {
    docElem.appendChild( div ).id = expando;
    return !document.getElementsByName || !document.getElementsByName( expando ).length;
  });

  // ID find and filter
  if ( support.getById ) {
    Expr.find["ID"] = function( id, context ) {
      if ( typeof context.getElementById !== "undefined" && documentIsHTML ) {
        var m = context.getElementById( id );
        return m ? [ m ] : [];
      }
    };
    Expr.filter["ID"] = function( id ) {
      var attrId = id.replace( runescape, funescape );
      return function( elem ) {
        return elem.getAttribute("id") === attrId;
      };
    };
  } else {
    // Support: IE6/7
    // getElementById is not reliable as a find shortcut
    delete Expr.find["ID"];

    Expr.filter["ID"] =  function( id ) {
      var attrId = id.replace( runescape, funescape );
      return function( elem ) {
        var node = typeof elem.getAttributeNode !== "undefined" &&
          elem.getAttributeNode("id");
        return node && node.value === attrId;
      };
    };
  }

  // Tag
  Expr.find["TAG"] = support.getElementsByTagName ?
    function( tag, context ) {
      if ( typeof context.getElementsByTagName !== "undefined" ) {
        return context.getElementsByTagName( tag );

      // DocumentFragment nodes don't have gEBTN
      } else if ( support.qsa ) {
        return context.querySelectorAll( tag );
      }
    } :

    function( tag, context ) {
      var elem,
        tmp = [],
        i = 0,
        // By happy coincidence, a (broken) gEBTN appears on DocumentFragment nodes too
        results = context.getElementsByTagName( tag );

      // Filter out possible comments
      if ( tag === "*" ) {
        while ( (elem = results[i++]) ) {
          if ( elem.nodeType === 1 ) {
            tmp.push( elem );
          }
        }

        return tmp;
      }
      return results;
    };

  // Class
  Expr.find["CLASS"] = support.getElementsByClassName && function( className, context ) {
    if ( typeof context.getElementsByClassName !== "undefined" && documentIsHTML ) {
      return context.getElementsByClassName( className );
    }
  };

  /* QSA/matchesSelector
  ---------------------------------------------------------------------- */

  // QSA and matchesSelector support

  // matchesSelector(:active) reports false when true (IE9/Opera 11.5)
  rbuggyMatches = [];

  // qSa(:focus) reports false when true (Chrome 21)
  // We allow this because of a bug in IE8/9 that throws an error
  // whenever `document.activeElement` is accessed on an iframe
  // So, we allow :focus to pass through QSA all the time to avoid the IE error
  // See http://bugs.jquery.com/ticket/13378
  rbuggyQSA = [];

  if ( (support.qsa = rnative.test( document.querySelectorAll )) ) {
    // Build QSA regex
    // Regex strategy adopted from Diego Perini
    assert(function( div ) {
      // Select is set to empty string on purpose
      // This is to test IE's treatment of not explicitly
      // setting a boolean content attribute,
      // since its presence should be enough
      // http://bugs.jquery.com/ticket/12359
      docElem.appendChild( div ).innerHTML = "<a id='" + expando + "'></a>" +
        "<select id='" + expando + "-\r\\' msallowcapture=''>" +
        "<option selected=''></option></select>";

      // Support: IE8, Opera 11-12.16
      // Nothing should be selected when empty strings follow ^= or $= or *=
      // The test attribute must be unknown in Opera but "safe" for WinRT
      // http://msdn.microsoft.com/en-us/library/ie/hh465388.aspx#attribute_section
      if ( div.querySelectorAll("[msallowcapture^='']").length ) {
        rbuggyQSA.push( "[*^$]=" + whitespace + "*(?:''|\"\")" );
      }

      // Support: IE8
      // Boolean attributes and "value" are not treated correctly
      if ( !div.querySelectorAll("[selected]").length ) {
        rbuggyQSA.push( "\\[" + whitespace + "*(?:value|" + booleans + ")" );
      }

      // Support: Chrome<29, Android<4.4, Safari<7.0+, iOS<7.0+, PhantomJS<1.9.8+
      if ( !div.querySelectorAll( "[id~=" + expando + "-]" ).length ) {
        rbuggyQSA.push("~=");
      }

      // Webkit/Opera - :checked should return selected option elements
      // http://www.w3.org/TR/2011/REC-css3-selectors-20110929/#checked
      // IE8 throws error here and will not see later tests
      if ( !div.querySelectorAll(":checked").length ) {
        rbuggyQSA.push(":checked");
      }

      // Support: Safari 8+, iOS 8+
      // https://bugs.webkit.org/show_bug.cgi?id=136851
      // In-page `selector#id sibing-combinator selector` fails
      if ( !div.querySelectorAll( "a#" + expando + "+*" ).length ) {
        rbuggyQSA.push(".#.+[+~]");
      }
    });

    assert(function( div ) {
      // Support: Windows 8 Native Apps
      // The type and name attributes are restricted during .innerHTML assignment
      var input = document.createElement("input");
      input.setAttribute( "type", "hidden" );
      div.appendChild( input ).setAttribute( "name", "D" );

      // Support: IE8
      // Enforce case-sensitivity of name attribute
      if ( div.querySelectorAll("[name=d]").length ) {
        rbuggyQSA.push( "name" + whitespace + "*[*^$|!~]?=" );
      }

      // FF 3.5 - :enabled/:disabled and hidden elements (hidden elements are still enabled)
      // IE8 throws error here and will not see later tests
      if ( !div.querySelectorAll(":enabled").length ) {
        rbuggyQSA.push( ":enabled", ":disabled" );
      }

      // Opera 10-11 does not throw on post-comma invalid pseudos
      div.querySelectorAll("*,:x");
      rbuggyQSA.push(",.*:");
    });
  }

  if ( (support.matchesSelector = rnative.test( (matches = docElem.matches ||
    docElem.webkitMatchesSelector ||
    docElem.mozMatchesSelector ||
    docElem.oMatchesSelector ||
    docElem.msMatchesSelector) )) ) {

    assert(function( div ) {
      // Check to see if it's possible to do matchesSelector
      // on a disconnected node (IE 9)
      support.disconnectedMatch = matches.call( div, "div" );

      // This should fail with an exception
      // Gecko does not error, returns false instead
      matches.call( div, "[s!='']:x" );
      rbuggyMatches.push( "!=", pseudos );
    });
  }

  rbuggyQSA = rbuggyQSA.length && new RegExp( rbuggyQSA.join("|") );
  rbuggyMatches = rbuggyMatches.length && new RegExp( rbuggyMatches.join("|") );

  /* Contains
  ---------------------------------------------------------------------- */
  hasCompare = rnative.test( docElem.compareDocumentPosition );

  // Element contains another
  // Purposefully self-exclusive
  // As in, an element does not contain itself
  contains = hasCompare || rnative.test( docElem.contains ) ?
    function( a, b ) {
      var adown = a.nodeType === 9 ? a.documentElement : a,
        bup = b && b.parentNode;
      return a === bup || !!( bup && bup.nodeType === 1 && (
        adown.contains ?
          adown.contains( bup ) :
          a.compareDocumentPosition && a.compareDocumentPosition( bup ) & 16
      ));
    } :
    function( a, b ) {
      if ( b ) {
        while ( (b = b.parentNode) ) {
          if ( b === a ) {
            return true;
          }
        }
      }
      return false;
    };

  /* Sorting
  ---------------------------------------------------------------------- */

  // Document order sorting
  sortOrder = hasCompare ?
  function( a, b ) {

    // Flag for duplicate removal
    if ( a === b ) {
      hasDuplicate = true;
      return 0;
    }

    // Sort on method existence if only one input has compareDocumentPosition
    var compare = !a.compareDocumentPosition - !b.compareDocumentPosition;
    if ( compare ) {
      return compare;
    }

    // Calculate position if both inputs belong to the same document
    compare = ( a.ownerDocument || a ) === ( b.ownerDocument || b ) ?
      a.compareDocumentPosition( b ) :

      // Otherwise we know they are disconnected
      1;

    // Disconnected nodes
    if ( compare & 1 ||
      (!support.sortDetached && b.compareDocumentPosition( a ) === compare) ) {

      // Choose the first element that is related to our preferred document
      if ( a === document || a.ownerDocument === preferredDoc && contains(preferredDoc, a) ) {
        return -1;
      }
      if ( b === document || b.ownerDocument === preferredDoc && contains(preferredDoc, b) ) {
        return 1;
      }

      // Maintain original order
      return sortInput ?
        ( indexOf( sortInput, a ) - indexOf( sortInput, b ) ) :
        0;
    }

    return compare & 4 ? -1 : 1;
  } :
  function( a, b ) {
    // Exit early if the nodes are identical
    if ( a === b ) {
      hasDuplicate = true;
      return 0;
    }

    var cur,
      i = 0,
      aup = a.parentNode,
      bup = b.parentNode,
      ap = [ a ],
      bp = [ b ];

    // Parentless nodes are either documents or disconnected
    if ( !aup || !bup ) {
      return a === document ? -1 :
        b === document ? 1 :
        aup ? -1 :
        bup ? 1 :
        sortInput ?
        ( indexOf( sortInput, a ) - indexOf( sortInput, b ) ) :
        0;

    // If the nodes are siblings, we can do a quick check
    } else if ( aup === bup ) {
      return siblingCheck( a, b );
    }

    // Otherwise we need full lists of their ancestors for comparison
    cur = a;
    while ( (cur = cur.parentNode) ) {
      ap.unshift( cur );
    }
    cur = b;
    while ( (cur = cur.parentNode) ) {
      bp.unshift( cur );
    }

    // Walk down the tree looking for a discrepancy
    while ( ap[i] === bp[i] ) {
      i++;
    }

    return i ?
      // Do a sibling check if the nodes have a common ancestor
      siblingCheck( ap[i], bp[i] ) :

      // Otherwise nodes in our document sort first
      ap[i] === preferredDoc ? -1 :
      bp[i] === preferredDoc ? 1 :
      0;
  };

  return document;
};

Sizzle.matches = function( expr, elements ) {
  return Sizzle( expr, null, null, elements );
};

Sizzle.matchesSelector = function( elem, expr ) {
  // Set document vars if needed
  if ( ( elem.ownerDocument || elem ) !== document ) {
    setDocument( elem );
  }

  // Make sure that attribute selectors are quoted
  expr = expr.replace( rattributeQuotes, "='$1']" );

  if ( support.matchesSelector && documentIsHTML &&
    !compilerCache[ expr + " " ] &&
    ( !rbuggyMatches || !rbuggyMatches.test( expr ) ) &&
    ( !rbuggyQSA     || !rbuggyQSA.test( expr ) ) ) {

    try {
      var ret = matches.call( elem, expr );

      // IE 9's matchesSelector returns false on disconnected nodes
      if ( ret || support.disconnectedMatch ||
          // As well, disconnected nodes are said to be in a document
          // fragment in IE 9
          elem.document && elem.document.nodeType !== 11 ) {
        return ret;
      }
    } catch (e) {}
  }

  return Sizzle( expr, document, null, [ elem ] ).length > 0;
};

Sizzle.contains = function( context, elem ) {
  // Set document vars if needed
  if ( ( context.ownerDocument || context ) !== document ) {
    setDocument( context );
  }
  return contains( context, elem );
};

Sizzle.attr = function( elem, name ) {
  // Set document vars if needed
  if ( ( elem.ownerDocument || elem ) !== document ) {
    setDocument( elem );
  }

  var fn = Expr.attrHandle[ name.toLowerCase() ],
    // Don't get fooled by Object.prototype properties (jQuery #13807)
    val = fn && hasOwn.call( Expr.attrHandle, name.toLowerCase() ) ?
      fn( elem, name, !documentIsHTML ) :
      undefined;

  return val !== undefined ?
    val :
    support.attributes || !documentIsHTML ?
      elem.getAttribute( name ) :
      (val = elem.getAttributeNode(name)) && val.specified ?
        val.value :
        null;
};

Sizzle.error = function( msg ) {
  throw new Error( "Syntax error, unrecognized expression: " + msg );
};

/**
 * Document sorting and removing duplicates
 * @param {ArrayLike} results
 */
Sizzle.uniqueSort = function( results ) {
  var elem,
    duplicates = [],
    j = 0,
    i = 0;

  // Unless we *know* we can detect duplicates, assume their presence
  hasDuplicate = !support.detectDuplicates;
  sortInput = !support.sortStable && results.slice( 0 );
  results.sort( sortOrder );

  if ( hasDuplicate ) {
    while ( (elem = results[i++]) ) {
      if ( elem === results[ i ] ) {
        j = duplicates.push( i );
      }
    }
    while ( j-- ) {
      results.splice( duplicates[ j ], 1 );
    }
  }

  // Clear input after sorting to release objects
  // See https://github.com/jquery/sizzle/pull/225
  sortInput = null;

  return results;
};

/**
 * Utility function for retrieving the text value of an array of DOM nodes
 * @param {Array|Element} elem
 */
getText = Sizzle.getText = function( elem ) {
  var node,
    ret = "",
    i = 0,
    nodeType = elem.nodeType;

  if ( !nodeType ) {
    // If no nodeType, this is expected to be an array
    while ( (node = elem[i++]) ) {
      // Do not traverse comment nodes
      ret += getText( node );
    }
  } else if ( nodeType === 1 || nodeType === 9 || nodeType === 11 ) {
    // Use textContent for elements
    // innerText usage removed for consistency of new lines (jQuery #11153)
    if ( typeof elem.textContent === "string" ) {
      return elem.textContent;
    } else {
      // Traverse its children
      for ( elem = elem.firstChild; elem; elem = elem.nextSibling ) {
        ret += getText( elem );
      }
    }
  } else if ( nodeType === 3 || nodeType === 4 ) {
    return elem.nodeValue;
  }
  // Do not include comment or processing instruction nodes

  return ret;
};

Expr = Sizzle.selectors = {

  // Can be adjusted by the user
  cacheLength: 50,

  createPseudo: markFunction,

  match: matchExpr,

  attrHandle: {},

  find: {},

  relative: {
    ">": { dir: "parentNode", first: true },
    " ": { dir: "parentNode" },
    "+": { dir: "previousSibling", first: true },
    "~": { dir: "previousSibling" }
  },

  preFilter: {
    "ATTR": function( match ) {
      match[1] = match[1].replace( runescape, funescape );

      // Move the given value to match[3] whether quoted or unquoted
      match[3] = ( match[3] || match[4] || match[5] || "" ).replace( runescape, funescape );

      if ( match[2] === "~=" ) {
        match[3] = " " + match[3] + " ";
      }

      return match.slice( 0, 4 );
    },

    "CHILD": function( match ) {
      /* matches from matchExpr["CHILD"]
        1 type (only|nth|...)
        2 what (child|of-type)
        3 argument (even|odd|\d*|\d*n([+-]\d+)?|...)
        4 xn-component of xn+y argument ([+-]?\d*n|)
        5 sign of xn-component
        6 x of xn-component
        7 sign of y-component
        8 y of y-component
      */
      match[1] = match[1].toLowerCase();

      if ( match[1].slice( 0, 3 ) === "nth" ) {
        // nth-* requires argument
        if ( !match[3] ) {
          Sizzle.error( match[0] );
        }

        // numeric x and y parameters for Expr.filter.CHILD
        // remember that false/true cast respectively to 0/1
        match[4] = +( match[4] ? match[5] + (match[6] || 1) : 2 * ( match[3] === "even" || match[3] === "odd" ) );
        match[5] = +( ( match[7] + match[8] ) || match[3] === "odd" );

      // other types prohibit arguments
      } else if ( match[3] ) {
        Sizzle.error( match[0] );
      }

      return match;
    },

    "PSEUDO": function( match ) {
      var excess,
        unquoted = !match[6] && match[2];

      if ( matchExpr["CHILD"].test( match[0] ) ) {
        return null;
      }

      // Accept quoted arguments as-is
      if ( match[3] ) {
        match[2] = match[4] || match[5] || "";

      // Strip excess characters from unquoted arguments
      } else if ( unquoted && rpseudo.test( unquoted ) &&
        // Get excess from tokenize (recursively)
        (excess = tokenize( unquoted, true )) &&
        // advance to the next closing parenthesis
        (excess = unquoted.indexOf( ")", unquoted.length - excess ) - unquoted.length) ) {

        // excess is a negative index
        match[0] = match[0].slice( 0, excess );
        match[2] = unquoted.slice( 0, excess );
      }

      // Return only captures needed by the pseudo filter method (type and argument)
      return match.slice( 0, 3 );
    }
  },

  filter: {

    "TAG": function( nodeNameSelector ) {
      var nodeName = nodeNameSelector.replace( runescape, funescape ).toLowerCase();
      return nodeNameSelector === "*" ?
        function() { return true; } :
        function( elem ) {
          return elem.nodeName && elem.nodeName.toLowerCase() === nodeName;
        };
    },

    "CLASS": function( className ) {
      var pattern = classCache[ className + " " ];

      return pattern ||
        (pattern = new RegExp( "(^|" + whitespace + ")" + className + "(" + whitespace + "|$)" )) &&
        classCache( className, function( elem ) {
          return pattern.test( typeof elem.className === "string" && elem.className || typeof elem.getAttribute !== "undefined" && elem.getAttribute("class") || "" );
        });
    },

    "ATTR": function( name, operator, check ) {
      return function( elem ) {
        var result = Sizzle.attr( elem, name );

        if ( result == null ) {
          return operator === "!=";
        }
        if ( !operator ) {
          return true;
        }

        result += "";

        return operator === "=" ? result === check :
          operator === "!=" ? result !== check :
          operator === "^=" ? check && result.indexOf( check ) === 0 :
          operator === "*=" ? check && result.indexOf( check ) > -1 :
          operator === "$=" ? check && result.slice( -check.length ) === check :
          operator === "~=" ? ( " " + result.replace( rwhitespace, " " ) + " " ).indexOf( check ) > -1 :
          operator === "|=" ? result === check || result.slice( 0, check.length + 1 ) === check + "-" :
          false;
      };
    },

    "CHILD": function( type, what, argument, first, last ) {
      var simple = type.slice( 0, 3 ) !== "nth",
        forward = type.slice( -4 ) !== "last",
        ofType = what === "of-type";

      return first === 1 && last === 0 ?

        // Shortcut for :nth-*(n)
        function( elem ) {
          return !!elem.parentNode;
        } :

        function( elem, context, xml ) {
          var cache, uniqueCache, outerCache, node, nodeIndex, start,
            dir = simple !== forward ? "nextSibling" : "previousSibling",
            parent = elem.parentNode,
            name = ofType && elem.nodeName.toLowerCase(),
            useCache = !xml && !ofType,
            diff = false;

          if ( parent ) {

            // :(first|last|only)-(child|of-type)
            if ( simple ) {
              while ( dir ) {
                node = elem;
                while ( (node = node[ dir ]) ) {
                  if ( ofType ?
                    node.nodeName.toLowerCase() === name :
                    node.nodeType === 1 ) {

                    return false;
                  }
                }
                // Reverse direction for :only-* (if we haven't yet done so)
                start = dir = type === "only" && !start && "nextSibling";
              }
              return true;
            }

            start = [ forward ? parent.firstChild : parent.lastChild ];

            // non-xml :nth-child(...) stores cache data on `parent`
            if ( forward && useCache ) {

              // Seek `elem` from a previously-cached index

              // ...in a gzip-friendly way
              node = parent;
              outerCache = node[ expando ] || (node[ expando ] = {});

              // Support: IE <9 only
              // Defend against cloned attroperties (jQuery gh-1709)
              uniqueCache = outerCache[ node.uniqueID ] ||
                (outerCache[ node.uniqueID ] = {});

              cache = uniqueCache[ type ] || [];
              nodeIndex = cache[ 0 ] === dirruns && cache[ 1 ];
              diff = nodeIndex && cache[ 2 ];
              node = nodeIndex && parent.childNodes[ nodeIndex ];

              while ( (node = ++nodeIndex && node && node[ dir ] ||

                // Fallback to seeking `elem` from the start
                (diff = nodeIndex = 0) || start.pop()) ) {

                // When found, cache indexes on `parent` and break
                if ( node.nodeType === 1 && ++diff && node === elem ) {
                  uniqueCache[ type ] = [ dirruns, nodeIndex, diff ];
                  break;
                }
              }

            } else {
              // Use previously-cached element index if available
              if ( useCache ) {
                // ...in a gzip-friendly way
                node = elem;
                outerCache = node[ expando ] || (node[ expando ] = {});

                // Support: IE <9 only
                // Defend against cloned attroperties (jQuery gh-1709)
                uniqueCache = outerCache[ node.uniqueID ] ||
                  (outerCache[ node.uniqueID ] = {});

                cache = uniqueCache[ type ] || [];
                nodeIndex = cache[ 0 ] === dirruns && cache[ 1 ];
                diff = nodeIndex;
              }

              // xml :nth-child(...)
              // or :nth-last-child(...) or :nth(-last)?-of-type(...)
              if ( diff === false ) {
                // Use the same loop as above to seek `elem` from the start
                while ( (node = ++nodeIndex && node && node[ dir ] ||
                  (diff = nodeIndex = 0) || start.pop()) ) {

                  if ( ( ofType ?
                    node.nodeName.toLowerCase() === name :
                    node.nodeType === 1 ) &&
                    ++diff ) {

                    // Cache the index of each encountered element
                    if ( useCache ) {
                      outerCache = node[ expando ] || (node[ expando ] = {});

                      // Support: IE <9 only
                      // Defend against cloned attroperties (jQuery gh-1709)
                      uniqueCache = outerCache[ node.uniqueID ] ||
                        (outerCache[ node.uniqueID ] = {});

                      uniqueCache[ type ] = [ dirruns, diff ];
                    }

                    if ( node === elem ) {
                      break;
                    }
                  }
                }
              }
            }

            // Incorporate the offset, then check against cycle size
            diff -= last;
            return diff === first || ( diff % first === 0 && diff / first >= 0 );
          }
        };
    },

    "PSEUDO": function( pseudo, argument ) {
      // pseudo-class names are case-insensitive
      // http://www.w3.org/TR/selectors/#pseudo-classes
      // Prioritize by case sensitivity in case custom pseudos are added with uppercase letters
      // Remember that setFilters inherits from pseudos
      var args,
        fn = Expr.pseudos[ pseudo ] || Expr.setFilters[ pseudo.toLowerCase() ] ||
          Sizzle.error( "unsupported pseudo: " + pseudo );

      // The user may use createPseudo to indicate that
      // arguments are needed to create the filter function
      // just as Sizzle does
      if ( fn[ expando ] ) {
        return fn( argument );
      }

      // But maintain support for old signatures
      if ( fn.length > 1 ) {
        args = [ pseudo, pseudo, "", argument ];
        return Expr.setFilters.hasOwnProperty( pseudo.toLowerCase() ) ?
          markFunction(function( seed, matches ) {
            var idx,
              matched = fn( seed, argument ),
              i = matched.length;
            while ( i-- ) {
              idx = indexOf( seed, matched[i] );
              seed[ idx ] = !( matches[ idx ] = matched[i] );
            }
          }) :
          function( elem ) {
            return fn( elem, 0, args );
          };
      }

      return fn;
    }
  },

  pseudos: {
    // Potentially complex pseudos
    "not": markFunction(function( selector ) {
      // Trim the selector passed to compile
      // to avoid treating leading and trailing
      // spaces as combinators
      var input = [],
        results = [],
        matcher = compile( selector.replace( rtrim, "$1" ) );

      return matcher[ expando ] ?
        markFunction(function( seed, matches, context, xml ) {
          var elem,
            unmatched = matcher( seed, null, xml, [] ),
            i = seed.length;

          // Match elements unmatched by `matcher`
          while ( i-- ) {
            if ( (elem = unmatched[i]) ) {
              seed[i] = !(matches[i] = elem);
            }
          }
        }) :
        function( elem, context, xml ) {
          input[0] = elem;
          matcher( input, null, xml, results );
          // Don't keep the element (issue #299)
          input[0] = null;
          return !results.pop();
        };
    }),

    "has": markFunction(function( selector ) {
      return function( elem ) {
        return Sizzle( selector, elem ).length > 0;
      };
    }),

    "contains": markFunction(function( text ) {
      text = text.replace( runescape, funescape );
      return function( elem ) {
        return ( elem.textContent || elem.innerText || getText( elem ) ).indexOf( text ) > -1;
      };
    }),

    // "Whether an element is represented by a :lang() selector
    // is based solely on the element's language value
    // being equal to the identifier C,
    // or beginning with the identifier C immediately followed by "-".
    // The matching of C against the element's language value is performed case-insensitively.
    // The identifier C does not have to be a valid language name."
    // http://www.w3.org/TR/selectors/#lang-pseudo
    "lang": markFunction( function( lang ) {
      // lang value must be a valid identifier
      if ( !ridentifier.test(lang || "") ) {
        Sizzle.error( "unsupported lang: " + lang );
      }
      lang = lang.replace( runescape, funescape ).toLowerCase();
      return function( elem ) {
        var elemLang;
        do {
          if ( (elemLang = documentIsHTML ?
            elem.lang :
            elem.getAttribute("xml:lang") || elem.getAttribute("lang")) ) {

            elemLang = elemLang.toLowerCase();
            return elemLang === lang || elemLang.indexOf( lang + "-" ) === 0;
          }
        } while ( (elem = elem.parentNode) && elem.nodeType === 1 );
        return false;
      };
    }),

    // Miscellaneous
    "target": function( elem ) {
      var hash = window.location && window.location.hash;
      return hash && hash.slice( 1 ) === elem.id;
    },

    "root": function( elem ) {
      return elem === docElem;
    },

    "focus": function( elem ) {
      return elem === document.activeElement && (!document.hasFocus || document.hasFocus()) && !!(elem.type || elem.href || ~elem.tabIndex);
    },

    // Boolean properties
    "enabled": function( elem ) {
      return elem.disabled === false;
    },

    "disabled": function( elem ) {
      return elem.disabled === true;
    },

    "checked": function( elem ) {
      // In CSS3, :checked should return both checked and selected elements
      // http://www.w3.org/TR/2011/REC-css3-selectors-20110929/#checked
      var nodeName = elem.nodeName.toLowerCase();
      return (nodeName === "input" && !!elem.checked) || (nodeName === "option" && !!elem.selected);
    },

    "selected": function( elem ) {
      // Accessing this property makes selected-by-default
      // options in Safari work properly
      if ( elem.parentNode ) {
        elem.parentNode.selectedIndex;
      }

      return elem.selected === true;
    },

    // Contents
    "empty": function( elem ) {
      // http://www.w3.org/TR/selectors/#empty-pseudo
      // :empty is negated by element (1) or content nodes (text: 3; cdata: 4; entity ref: 5),
      //   but not by others (comment: 8; processing instruction: 7; etc.)
      // nodeType < 6 works because attributes (2) do not appear as children
      for ( elem = elem.firstChild; elem; elem = elem.nextSibling ) {
        if ( elem.nodeType < 6 ) {
          return false;
        }
      }
      return true;
    },

    "parent": function( elem ) {
      return !Expr.pseudos["empty"]( elem );
    },

    // Element/input types
    "header": function( elem ) {
      return rheader.test( elem.nodeName );
    },

    "input": function( elem ) {
      return rinputs.test( elem.nodeName );
    },

    "button": function( elem ) {
      var name = elem.nodeName.toLowerCase();
      return name === "input" && elem.type === "button" || name === "button";
    },

    "text": function( elem ) {
      var attr;
      return elem.nodeName.toLowerCase() === "input" &&
        elem.type === "text" &&

        // Support: IE<8
        // New HTML5 attribute values (e.g., "search") appear with elem.type === "text"
        ( (attr = elem.getAttribute("type")) == null || attr.toLowerCase() === "text" );
    },

    // Position-in-collection
    "first": createPositionalPseudo(function() {
      return [ 0 ];
    }),

    "last": createPositionalPseudo(function( matchIndexes, length ) {
      return [ length - 1 ];
    }),

    "eq": createPositionalPseudo(function( matchIndexes, length, argument ) {
      return [ argument < 0 ? argument + length : argument ];
    }),

    "even": createPositionalPseudo(function( matchIndexes, length ) {
      var i = 0;
      for ( ; i < length; i += 2 ) {
        matchIndexes.push( i );
      }
      return matchIndexes;
    }),

    "odd": createPositionalPseudo(function( matchIndexes, length ) {
      var i = 1;
      for ( ; i < length; i += 2 ) {
        matchIndexes.push( i );
      }
      return matchIndexes;
    }),

    "lt": createPositionalPseudo(function( matchIndexes, length, argument ) {
      var i = argument < 0 ? argument + length : argument;
      for ( ; --i >= 0; ) {
        matchIndexes.push( i );
      }
      return matchIndexes;
    }),

    "gt": createPositionalPseudo(function( matchIndexes, length, argument ) {
      var i = argument < 0 ? argument + length : argument;
      for ( ; ++i < length; ) {
        matchIndexes.push( i );
      }
      return matchIndexes;
    })
  }
};

Expr.pseudos["nth"] = Expr.pseudos["eq"];

// Add button/input type pseudos
for ( i in { radio: true, checkbox: true, file: true, password: true, image: true } ) {
  Expr.pseudos[ i ] = createInputPseudo( i );
}
for ( i in { submit: true, reset: true } ) {
  Expr.pseudos[ i ] = createButtonPseudo( i );
}

// Easy API for creating new setFilters
function setFilters() {}
setFilters.prototype = Expr.filters = Expr.pseudos;
Expr.setFilters = new setFilters();

tokenize = Sizzle.tokenize = function( selector, parseOnly ) {
  var matched, match, tokens, type,
    soFar, groups, preFilters,
    cached = tokenCache[ selector + " " ];

  if ( cached ) {
    return parseOnly ? 0 : cached.slice( 0 );
  }

  soFar = selector;
  groups = [];
  preFilters = Expr.preFilter;

  while ( soFar ) {

    // Comma and first run
    if ( !matched || (match = rcomma.exec( soFar )) ) {
      if ( match ) {
        // Don't consume trailing commas as valid
        soFar = soFar.slice( match[0].length ) || soFar;
      }
      groups.push( (tokens = []) );
    }

    matched = false;

    // Combinators
    if ( (match = rcombinators.exec( soFar )) ) {
      matched = match.shift();
      tokens.push({
        value: matched,
        // Cast descendant combinators to space
        type: match[0].replace( rtrim, " " )
      });
      soFar = soFar.slice( matched.length );
    }

    // Filters
    for ( type in Expr.filter ) {
      if ( (match = matchExpr[ type ].exec( soFar )) && (!preFilters[ type ] ||
        (match = preFilters[ type ]( match ))) ) {
        matched = match.shift();
        tokens.push({
          value: matched,
          type: type,
          matches: match
        });
        soFar = soFar.slice( matched.length );
      }
    }

    if ( !matched ) {
      break;
    }
  }

  // Return the length of the invalid excess
  // if we're just parsing
  // Otherwise, throw an error or return tokens
  return parseOnly ?
    soFar.length :
    soFar ?
      Sizzle.error( selector ) :
      // Cache the tokens
      tokenCache( selector, groups ).slice( 0 );
};

function toSelector( tokens ) {
  var i = 0,
    len = tokens.length,
    selector = "";
  for ( ; i < len; i++ ) {
    selector += tokens[i].value;
  }
  return selector;
}

function addCombinator( matcher, combinator, base ) {
  var dir = combinator.dir,
    checkNonElements = base && dir === "parentNode",
    doneName = done++;

  return combinator.first ?
    // Check against closest ancestor/preceding element
    function( elem, context, xml ) {
      while ( (elem = elem[ dir ]) ) {
        if ( elem.nodeType === 1 || checkNonElements ) {
          return matcher( elem, context, xml );
        }
      }
    } :

    // Check against all ancestor/preceding elements
    function( elem, context, xml ) {
      var oldCache, uniqueCache, outerCache,
        newCache = [ dirruns, doneName ];

      // We can't set arbitrary data on XML nodes, so they don't benefit from combinator caching
      if ( xml ) {
        while ( (elem = elem[ dir ]) ) {
          if ( elem.nodeType === 1 || checkNonElements ) {
            if ( matcher( elem, context, xml ) ) {
              return true;
            }
          }
        }
      } else {
        while ( (elem = elem[ dir ]) ) {
          if ( elem.nodeType === 1 || checkNonElements ) {
            outerCache = elem[ expando ] || (elem[ expando ] = {});

            // Support: IE <9 only
            // Defend against cloned attroperties (jQuery gh-1709)
            uniqueCache = outerCache[ elem.uniqueID ] || (outerCache[ elem.uniqueID ] = {});

            if ( (oldCache = uniqueCache[ dir ]) &&
              oldCache[ 0 ] === dirruns && oldCache[ 1 ] === doneName ) {

              // Assign to newCache so results back-propagate to previous elements
              return (newCache[ 2 ] = oldCache[ 2 ]);
            } else {
              // Reuse newcache so results back-propagate to previous elements
              uniqueCache[ dir ] = newCache;

              // A match means we're done; a fail means we have to keep checking
              if ( (newCache[ 2 ] = matcher( elem, context, xml )) ) {
                return true;
              }
            }
          }
        }
      }
    };
}

function elementMatcher( matchers ) {
  return matchers.length > 1 ?
    function( elem, context, xml ) {
      var i = matchers.length;
      while ( i-- ) {
        if ( !matchers[i]( elem, context, xml ) ) {
          return false;
        }
      }
      return true;
    } :
    matchers[0];
}

function multipleContexts( selector, contexts, results ) {
  var i = 0,
    len = contexts.length;
  for ( ; i < len; i++ ) {
    Sizzle( selector, contexts[i], results );
  }
  return results;
}

function condense( unmatched, map, filter, context, xml ) {
  var elem,
    newUnmatched = [],
    i = 0,
    len = unmatched.length,
    mapped = map != null;

  for ( ; i < len; i++ ) {
    if ( (elem = unmatched[i]) ) {
      if ( !filter || filter( elem, context, xml ) ) {
        newUnmatched.push( elem );
        if ( mapped ) {
          map.push( i );
        }
      }
    }
  }

  return newUnmatched;
}

function setMatcher( preFilter, selector, matcher, postFilter, postFinder, postSelector ) {
  if ( postFilter && !postFilter[ expando ] ) {
    postFilter = setMatcher( postFilter );
  }
  if ( postFinder && !postFinder[ expando ] ) {
    postFinder = setMatcher( postFinder, postSelector );
  }
  return markFunction(function( seed, results, context, xml ) {
    var temp, i, elem,
      preMap = [],
      postMap = [],
      preexisting = results.length,

      // Get initial elements from seed or context
      elems = seed || multipleContexts( selector || "*", context.nodeType ? [ context ] : context, [] ),

      // Prefilter to get matcher input, preserving a map for seed-results synchronization
      matcherIn = preFilter && ( seed || !selector ) ?
        condense( elems, preMap, preFilter, context, xml ) :
        elems,

      matcherOut = matcher ?
        // If we have a postFinder, or filtered seed, or non-seed postFilter or preexisting results,
        postFinder || ( seed ? preFilter : preexisting || postFilter ) ?

          // ...intermediate processing is necessary
          [] :

          // ...otherwise use results directly
          results :
        matcherIn;

    // Find primary matches
    if ( matcher ) {
      matcher( matcherIn, matcherOut, context, xml );
    }

    // Apply postFilter
    if ( postFilter ) {
      temp = condense( matcherOut, postMap );
      postFilter( temp, [], context, xml );

      // Un-match failing elements by moving them back to matcherIn
      i = temp.length;
      while ( i-- ) {
        if ( (elem = temp[i]) ) {
          matcherOut[ postMap[i] ] = !(matcherIn[ postMap[i] ] = elem);
        }
      }
    }

    if ( seed ) {
      if ( postFinder || preFilter ) {
        if ( postFinder ) {
          // Get the final matcherOut by condensing this intermediate into postFinder contexts
          temp = [];
          i = matcherOut.length;
          while ( i-- ) {
            if ( (elem = matcherOut[i]) ) {
              // Restore matcherIn since elem is not yet a final match
              temp.push( (matcherIn[i] = elem) );
            }
          }
          postFinder( null, (matcherOut = []), temp, xml );
        }

        // Move matched elements from seed to results to keep them synchronized
        i = matcherOut.length;
        while ( i-- ) {
          if ( (elem = matcherOut[i]) &&
            (temp = postFinder ? indexOf( seed, elem ) : preMap[i]) > -1 ) {

            seed[temp] = !(results[temp] = elem);
          }
        }
      }

    // Add elements to results, through postFinder if defined
    } else {
      matcherOut = condense(
        matcherOut === results ?
          matcherOut.splice( preexisting, matcherOut.length ) :
          matcherOut
      );
      if ( postFinder ) {
        postFinder( null, results, matcherOut, xml );
      } else {
        push.apply( results, matcherOut );
      }
    }
  });
}

function matcherFromTokens( tokens ) {
  var checkContext, matcher, j,
    len = tokens.length,
    leadingRelative = Expr.relative[ tokens[0].type ],
    implicitRelative = leadingRelative || Expr.relative[" "],
    i = leadingRelative ? 1 : 0,

    // The foundational matcher ensures that elements are reachable from top-level context(s)
    matchContext = addCombinator( function( elem ) {
      return elem === checkContext;
    }, implicitRelative, true ),
    matchAnyContext = addCombinator( function( elem ) {
      return indexOf( checkContext, elem ) > -1;
    }, implicitRelative, true ),
    matchers = [ function( elem, context, xml ) {
      var ret = ( !leadingRelative && ( xml || context !== outermostContext ) ) || (
        (checkContext = context).nodeType ?
          matchContext( elem, context, xml ) :
          matchAnyContext( elem, context, xml ) );
      // Avoid hanging onto element (issue #299)
      checkContext = null;
      return ret;
    } ];

  for ( ; i < len; i++ ) {
    if ( (matcher = Expr.relative[ tokens[i].type ]) ) {
      matchers = [ addCombinator(elementMatcher( matchers ), matcher) ];
    } else {
      matcher = Expr.filter[ tokens[i].type ].apply( null, tokens[i].matches );

      // Return special upon seeing a positional matcher
      if ( matcher[ expando ] ) {
        // Find the next relative operator (if any) for proper handling
        j = ++i;
        for ( ; j < len; j++ ) {
          if ( Expr.relative[ tokens[j].type ] ) {
            break;
          }
        }
        return setMatcher(
          i > 1 && elementMatcher( matchers ),
          i > 1 && toSelector(
            // If the preceding token was a descendant combinator, insert an implicit any-element `*`
            tokens.slice( 0, i - 1 ).concat({ value: tokens[ i - 2 ].type === " " ? "*" : "" })
          ).replace( rtrim, "$1" ),
          matcher,
          i < j && matcherFromTokens( tokens.slice( i, j ) ),
          j < len && matcherFromTokens( (tokens = tokens.slice( j )) ),
          j < len && toSelector( tokens )
        );
      }
      matchers.push( matcher );
    }
  }

  return elementMatcher( matchers );
}

function matcherFromGroupMatchers( elementMatchers, setMatchers ) {
  var bySet = setMatchers.length > 0,
    byElement = elementMatchers.length > 0,
    superMatcher = function( seed, context, xml, results, outermost ) {
      var elem, j, matcher,
        matchedCount = 0,
        i = "0",
        unmatched = seed && [],
        setMatched = [],
        contextBackup = outermostContext,
        // We must always have either seed elements or outermost context
        elems = seed || byElement && Expr.find["TAG"]( "*", outermost ),
        // Use integer dirruns iff this is the outermost matcher
        dirrunsUnique = (dirruns += contextBackup == null ? 1 : Math.random() || 0.1),
        len = elems.length;

      if ( outermost ) {
        outermostContext = context === document || context || outermost;
      }

      // Add elements passing elementMatchers directly to results
      // Support: IE<9, Safari
      // Tolerate NodeList properties (IE: "length"; Safari: <number>) matching elements by id
      for ( ; i !== len && (elem = elems[i]) != null; i++ ) {
        if ( byElement && elem ) {
          j = 0;
          if ( !context && elem.ownerDocument !== document ) {
            setDocument( elem );
            xml = !documentIsHTML;
          }
          while ( (matcher = elementMatchers[j++]) ) {
            if ( matcher( elem, context || document, xml) ) {
              results.push( elem );
              break;
            }
          }
          if ( outermost ) {
            dirruns = dirrunsUnique;
          }
        }

        // Track unmatched elements for set filters
        if ( bySet ) {
          // They will have gone through all possible matchers
          if ( (elem = !matcher && elem) ) {
            matchedCount--;
          }

          // Lengthen the array for every element, matched or not
          if ( seed ) {
            unmatched.push( elem );
          }
        }
      }

      // `i` is now the count of elements visited above, and adding it to `matchedCount`
      // makes the latter nonnegative.
      matchedCount += i;

      // Apply set filters to unmatched elements
      // NOTE: This can be skipped if there are no unmatched elements (i.e., `matchedCount`
      // equals `i`), unless we didn't visit _any_ elements in the above loop because we have
      // no element matchers and no seed.
      // Incrementing an initially-string "0" `i` allows `i` to remain a string only in that
      // case, which will result in a "00" `matchedCount` that differs from `i` but is also
      // numerically zero.
      if ( bySet && i !== matchedCount ) {
        j = 0;
        while ( (matcher = setMatchers[j++]) ) {
          matcher( unmatched, setMatched, context, xml );
        }

        if ( seed ) {
          // Reintegrate element matches to eliminate the need for sorting
          if ( matchedCount > 0 ) {
            while ( i-- ) {
              if ( !(unmatched[i] || setMatched[i]) ) {
                setMatched[i] = pop.call( results );
              }
            }
          }

          // Discard index placeholder values to get only actual matches
          setMatched = condense( setMatched );
        }

        // Add matches to results
        push.apply( results, setMatched );

        // Seedless set matches succeeding multiple successful matchers stipulate sorting
        if ( outermost && !seed && setMatched.length > 0 &&
          ( matchedCount + setMatchers.length ) > 1 ) {

          Sizzle.uniqueSort( results );
        }
      }

      // Override manipulation of globals by nested matchers
      if ( outermost ) {
        dirruns = dirrunsUnique;
        outermostContext = contextBackup;
      }

      return unmatched;
    };

  return bySet ?
    markFunction( superMatcher ) :
    superMatcher;
}

compile = Sizzle.compile = function( selector, match /* Internal Use Only */ ) {
  var i,
    setMatchers = [],
    elementMatchers = [],
    cached = compilerCache[ selector + " " ];

  if ( !cached ) {
    // Generate a function of recursive functions that can be used to check each element
    if ( !match ) {
      match = tokenize( selector );
    }
    i = match.length;
    while ( i-- ) {
      cached = matcherFromTokens( match[i] );
      if ( cached[ expando ] ) {
        setMatchers.push( cached );
      } else {
        elementMatchers.push( cached );
      }
    }

    // Cache the compiled function
    cached = compilerCache( selector, matcherFromGroupMatchers( elementMatchers, setMatchers ) );

    // Save selector and tokenization
    cached.selector = selector;
  }
  return cached;
};

/**
 * A low-level selection function that works with Sizzle's compiled
 *  selector functions
 * @param {String|Function} selector A selector or a pre-compiled
 *  selector function built with Sizzle.compile
 * @param {Element} context
 * @param {Array} [results]
 * @param {Array} [seed] A set of elements to match against
 */
select = Sizzle.select = function( selector, context, results, seed ) {
  var i, tokens, token, type, find,
    compiled = typeof selector === "function" && selector,
    match = !seed && tokenize( (selector = compiled.selector || selector) );

  results = results || [];

  // Try to minimize operations if there is only one selector in the list and no seed
  // (the latter of which guarantees us context)
  if ( match.length === 1 ) {

    // Reduce context if the leading compound selector is an ID
    tokens = match[0] = match[0].slice( 0 );
    if ( tokens.length > 2 && (token = tokens[0]).type === "ID" &&
        support.getById && context.nodeType === 9 && documentIsHTML &&
        Expr.relative[ tokens[1].type ] ) {

      context = ( Expr.find["ID"]( token.matches[0].replace(runescape, funescape), context ) || [] )[0];
      if ( !context ) {
        return results;

      // Precompiled matchers will still verify ancestry, so step up a level
      } else if ( compiled ) {
        context = context.parentNode;
      }

      selector = selector.slice( tokens.shift().value.length );
    }

    // Fetch a seed set for right-to-left matching
    i = matchExpr["needsContext"].test( selector ) ? 0 : tokens.length;
    while ( i-- ) {
      token = tokens[i];

      // Abort if we hit a combinator
      if ( Expr.relative[ (type = token.type) ] ) {
        break;
      }
      if ( (find = Expr.find[ type ]) ) {
        // Search, expanding context for leading sibling combinators
        if ( (seed = find(
          token.matches[0].replace( runescape, funescape ),
          rsibling.test( tokens[0].type ) && testContext( context.parentNode ) || context
        )) ) {

          // If seed is empty or no tokens remain, we can return early
          tokens.splice( i, 1 );
          selector = seed.length && toSelector( tokens );
          if ( !selector ) {
            push.apply( results, seed );
            return results;
          }

          break;
        }
      }
    }
  }

  // Compile and execute a filtering function if one is not provided
  // Provide `match` to avoid retokenization if we modified the selector above
  ( compiled || compile( selector, match ) )(
    seed,
    context,
    !documentIsHTML,
    results,
    !context || rsibling.test( selector ) && testContext( context.parentNode ) || context
  );
  return results;
};

// One-time assignments

// Sort stability
support.sortStable = expando.split("").sort( sortOrder ).join("") === expando;

// Support: Chrome 14-35+
// Always assume duplicates if they aren't passed to the comparison function
support.detectDuplicates = !!hasDuplicate;

// Initialize against the default document
setDocument();

// Support: Webkit<537.32 - Safari 6.0.3/Chrome 25 (fixed in Chrome 27)
// Detached nodes confoundingly follow *each other*
support.sortDetached = assert(function( div1 ) {
  // Should return 1, but returns 4 (following)
  return div1.compareDocumentPosition( document.createElement("div") ) & 1;
});

// Support: IE<8
// Prevent attribute/property "interpolation"
// http://msdn.microsoft.com/en-us/library/ms536429%28VS.85%29.aspx
if ( !assert(function( div ) {
  div.innerHTML = "<a href='#'></a>";
  return div.firstChild.getAttribute("href") === "#" ;
}) ) {
  addHandle( "type|href|height|width", function( elem, name, isXML ) {
    if ( !isXML ) {
      return elem.getAttribute( name, name.toLowerCase() === "type" ? 1 : 2 );
    }
  });
}

// Support: IE<9
// Use defaultValue in place of getAttribute("value")
if ( !support.attributes || !assert(function( div ) {
  div.innerHTML = "<input/>";
  div.firstChild.setAttribute( "value", "" );
  return div.firstChild.getAttribute( "value" ) === "";
}) ) {
  addHandle( "value", function( elem, name, isXML ) {
    if ( !isXML && elem.nodeName.toLowerCase() === "input" ) {
      return elem.defaultValue;
    }
  });
}

// Support: IE<9
// Use getAttributeNode to fetch booleans when getAttribute lies
if ( !assert(function( div ) {
  return div.getAttribute("disabled") == null;
}) ) {
  addHandle( booleans, function( elem, name, isXML ) {
    var val;
    if ( !isXML ) {
      return elem[ name ] === true ? name.toLowerCase() :
          (val = elem.getAttributeNode( name )) && val.specified ?
          val.value :
        null;
    }
  });
}

return Sizzle;

})( window );



jQuery.find = Sizzle;
jQuery.expr = Sizzle.selectors;
jQuery.expr[ ":" ] = jQuery.expr.pseudos;
jQuery.uniqueSort = jQuery.unique = Sizzle.uniqueSort;
jQuery.text = Sizzle.getText;
jQuery.isXMLDoc = Sizzle.isXML;
jQuery.contains = Sizzle.contains;



var dir = function( elem, dir, until ) {
  var matched = [],
    truncate = until !== undefined;

  while ( ( elem = elem[ dir ] ) && elem.nodeType !== 9 ) {
    if ( elem.nodeType === 1 ) {
      if ( truncate && jQuery( elem ).is( until ) ) {
        break;
      }
      matched.push( elem );
    }
  }
  return matched;
};


var siblings = function( n, elem ) {
  var matched = [];

  for ( ; n; n = n.nextSibling ) {
    if ( n.nodeType === 1 && n !== elem ) {
      matched.push( n );
    }
  }

  return matched;
};


var rneedsContext = jQuery.expr.match.needsContext;

var rsingleTag = ( /^<([\w-]+)\s*\/?>(?:<\/\1>|)$/ );



var risSimple = /^.[^:#\[\.,]*$/;

// Implement the identical functionality for filter and not
function winnow( elements, qualifier, not ) {
  if ( jQuery.isFunction( qualifier ) ) {
    return jQuery.grep( elements, function( elem, i ) {
      /* jshint -W018 */
      return !!qualifier.call( elem, i, elem ) !== not;
    } );

  }

  if ( qualifier.nodeType ) {
    return jQuery.grep( elements, function( elem ) {
      return ( elem === qualifier ) !== not;
    } );

  }

  if ( typeof qualifier === "string" ) {
    if ( risSimple.test( qualifier ) ) {
      return jQuery.filter( qualifier, elements, not );
    }

    qualifier = jQuery.filter( qualifier, elements );
  }

  return jQuery.grep( elements, function( elem ) {
    return ( indexOf.call( qualifier, elem ) > -1 ) !== not;
  } );
}

jQuery.filter = function( expr, elems, not ) {
  var elem = elems[ 0 ];

  if ( not ) {
    expr = ":not(" + expr + ")";
  }

  return elems.length === 1 && elem.nodeType === 1 ?
    jQuery.find.matchesSelector( elem, expr ) ? [ elem ] : [] :
    jQuery.find.matches( expr, jQuery.grep( elems, function( elem ) {
      return elem.nodeType === 1;
    } ) );
};

jQuery.fn.extend( {
  find: function( selector ) {
    var i,
      len = this.length,
      ret = [],
      self = this;

    if ( typeof selector !== "string" ) {
      return this.pushStack( jQuery( selector ).filter( function() {
        for ( i = 0; i < len; i++ ) {
          if ( jQuery.contains( self[ i ], this ) ) {
            return true;
          }
        }
      } ) );
    }

    for ( i = 0; i < len; i++ ) {
      jQuery.find( selector, self[ i ], ret );
    }

    // Needed because $( selector, context ) becomes $( context ).find( selector )
    ret = this.pushStack( len > 1 ? jQuery.unique( ret ) : ret );
    ret.selector = this.selector ? this.selector + " " + selector : selector;
    return ret;
  },
  filter: function( selector ) {
    return this.pushStack( winnow( this, selector || [], false ) );
  },
  not: function( selector ) {
    return this.pushStack( winnow( this, selector || [], true ) );
  },
  is: function( selector ) {
    return !!winnow(
      this,

      // If this is a positional/relative selector, check membership in the returned set
      // so $("p:first").is("p:last") won't return true for a doc with two "p".
      typeof selector === "string" && rneedsContext.test( selector ) ?
        jQuery( selector ) :
        selector || [],
      false
    ).length;
  }
} );


// Initialize a jQuery object


// A central reference to the root jQuery(document)
var rootjQuery,

  // A simple way to check for HTML strings
  // Prioritize #id over <tag> to avoid XSS via location.hash (#9521)
  // Strict HTML recognition (#11290: must start with <)
  rquickExpr = /^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/,

  init = jQuery.fn.init = function( selector, context, root ) {
    var match, elem;

    // HANDLE: $(""), $(null), $(undefined), $(false)
    if ( !selector ) {
      return this;
    }

    // Method init() accepts an alternate rootjQuery
    // so migrate can support jQuery.sub (gh-2101)
    root = root || rootjQuery;

    // Handle HTML strings
    if ( typeof selector === "string" ) {
      if ( selector[ 0 ] === "<" &&
        selector[ selector.length - 1 ] === ">" &&
        selector.length >= 3 ) {

        // Assume that strings that start and end with <> are HTML and skip the regex check
        match = [ null, selector, null ];

      } else {
        match = rquickExpr.exec( selector );
      }

      // Match html or make sure no context is specified for #id
      if ( match && ( match[ 1 ] || !context ) ) {

        // HANDLE: $(html) -> $(array)
        if ( match[ 1 ] ) {
          context = context instanceof jQuery ? context[ 0 ] : context;

          // Option to run scripts is true for back-compat
          // Intentionally let the error be thrown if parseHTML is not present
          jQuery.merge( this, jQuery.parseHTML(
            match[ 1 ],
            context && context.nodeType ? context.ownerDocument || context : document,
            true
          ) );

          // HANDLE: $(html, props)
          if ( rsingleTag.test( match[ 1 ] ) && jQuery.isPlainObject( context ) ) {
            for ( match in context ) {

              // Properties of context are called as methods if possible
              if ( jQuery.isFunction( this[ match ] ) ) {
                this[ match ]( context[ match ] );

              // ...and otherwise set as attributes
              } else {
                this.attr( match, context[ match ] );
              }
            }
          }

          return this;

        // HANDLE: $(#id)
        } else {
          elem = document.getElementById( match[ 2 ] );

          // Support: Blackberry 4.6
          // gEBID returns nodes no longer in the document (#6963)
          if ( elem && elem.parentNode ) {

            // Inject the element directly into the jQuery object
            this.length = 1;
            this[ 0 ] = elem;
          }

          this.context = document;
          this.selector = selector;
          return this;
        }

      // HANDLE: $(expr, $(...))
      } else if ( !context || context.jquery ) {
        return ( context || root ).find( selector );

      // HANDLE: $(expr, context)
      // (which is just equivalent to: $(context).find(expr)
      } else {
        return this.constructor( context ).find( selector );
      }

    // HANDLE: $(DOMElement)
    } else if ( selector.nodeType ) {
      this.context = this[ 0 ] = selector;
      this.length = 1;
      return this;

    // HANDLE: $(function)
    // Shortcut for document ready
    } else if ( jQuery.isFunction( selector ) ) {
      return root.ready !== undefined ?
        root.ready( selector ) :

        // Execute immediately if ready is not present
        selector( jQuery );
    }

    if ( selector.selector !== undefined ) {
      this.selector = selector.selector;
      this.context = selector.context;
    }

    return jQuery.makeArray( selector, this );
  };

// Give the init function the jQuery prototype for later instantiation
init.prototype = jQuery.fn;

// Initialize central reference
rootjQuery = jQuery( document );


var rparentsprev = /^(?:parents|prev(?:Until|All))/,

  // Methods guaranteed to produce a unique set when starting from a unique set
  guaranteedUnique = {
    children: true,
    contents: true,
    next: true,
    prev: true
  };

jQuery.fn.extend( {
  has: function( target ) {
    var targets = jQuery( target, this ),
      l = targets.length;

    return this.filter( function() {
      var i = 0;
      for ( ; i < l; i++ ) {
        if ( jQuery.contains( this, targets[ i ] ) ) {
          return true;
        }
      }
    } );
  },

  closest: function( selectors, context ) {
    var cur,
      i = 0,
      l = this.length,
      matched = [],
      pos = rneedsContext.test( selectors ) || typeof selectors !== "string" ?
        jQuery( selectors, context || this.context ) :
        0;

    for ( ; i < l; i++ ) {
      for ( cur = this[ i ]; cur && cur !== context; cur = cur.parentNode ) {

        // Always skip document fragments
        if ( cur.nodeType < 11 && ( pos ?
          pos.index( cur ) > -1 :

          // Don't pass non-elements to Sizzle
          cur.nodeType === 1 &&
            jQuery.find.matchesSelector( cur, selectors ) ) ) {

          matched.push( cur );
          break;
        }
      }
    }

    return this.pushStack( matched.length > 1 ? jQuery.uniqueSort( matched ) : matched );
  },

  // Determine the position of an element within the set
  index: function( elem ) {

    // No argument, return index in parent
    if ( !elem ) {
      return ( this[ 0 ] && this[ 0 ].parentNode ) ? this.first().prevAll().length : -1;
    }

    // Index in selector
    if ( typeof elem === "string" ) {
      return indexOf.call( jQuery( elem ), this[ 0 ] );
    }

    // Locate the position of the desired element
    return indexOf.call( this,

      // If it receives a jQuery object, the first element is used
      elem.jquery ? elem[ 0 ] : elem
    );
  },

  add: function( selector, context ) {
    return this.pushStack(
      jQuery.uniqueSort(
        jQuery.merge( this.get(), jQuery( selector, context ) )
      )
    );
  },

  addBack: function( selector ) {
    return this.add( selector == null ?
      this.prevObject : this.prevObject.filter( selector )
    );
  }
} );

function sibling( cur, dir ) {
  while ( ( cur = cur[ dir ] ) && cur.nodeType !== 1 ) {}
  return cur;
}

jQuery.each( {
  parent: function( elem ) {
    var parent = elem.parentNode;
    return parent && parent.nodeType !== 11 ? parent : null;
  },
  parents: function( elem ) {
    return dir( elem, "parentNode" );
  },
  parentsUntil: function( elem, i, until ) {
    return dir( elem, "parentNode", until );
  },
  next: function( elem ) {
    return sibling( elem, "nextSibling" );
  },
  prev: function( elem ) {
    return sibling( elem, "previousSibling" );
  },
  nextAll: function( elem ) {
    return dir( elem, "nextSibling" );
  },
  prevAll: function( elem ) {
    return dir( elem, "previousSibling" );
  },
  nextUntil: function( elem, i, until ) {
    return dir( elem, "nextSibling", until );
  },
  prevUntil: function( elem, i, until ) {
    return dir( elem, "previousSibling", until );
  },
  siblings: function( elem ) {
    return siblings( ( elem.parentNode || {} ).firstChild, elem );
  },
  children: function( elem ) {
    return siblings( elem.firstChild );
  },
  contents: function( elem ) {
    return elem.contentDocument || jQuery.merge( [], elem.childNodes );
  }
}, function( name, fn ) {
  jQuery.fn[ name ] = function( until, selector ) {
    var matched = jQuery.map( this, fn, until );

    if ( name.slice( -5 ) !== "Until" ) {
      selector = until;
    }

    if ( selector && typeof selector === "string" ) {
      matched = jQuery.filter( selector, matched );
    }

    if ( this.length > 1 ) {

      // Remove duplicates
      if ( !guaranteedUnique[ name ] ) {
        jQuery.uniqueSort( matched );
      }

      // Reverse order for parents* and prev-derivatives
      if ( rparentsprev.test( name ) ) {
        matched.reverse();
      }
    }

    return this.pushStack( matched );
  };
} );
var rnotwhite = ( /\S+/g );



// Convert String-formatted options into Object-formatted ones
function createOptions( options ) {
  var object = {};
  jQuery.each( options.match( rnotwhite ) || [], function( _, flag ) {
    object[ flag ] = true;
  } );
  return object;
}

/*
 * Create a callback list using the following parameters:
 *
 *  options: an optional list of space-separated options that will change how
 *      the callback list behaves or a more traditional option object
 *
 * By default a callback list will act like an event callback list and can be
 * "fired" multiple times.
 *
 * Possible options:
 *
 *  once:     will ensure the callback list can only be fired once (like a Deferred)
 *
 *  memory:     will keep track of previous values and will call any callback added
 *          after the list has been fired right away with the latest "memorized"
 *          values (like a Deferred)
 *
 *  unique:     will ensure a callback can only be added once (no duplicate in the list)
 *
 *  stopOnFalse:  interrupt callings when a callback returns false
 *
 */
jQuery.Callbacks = function( options ) {

  // Convert options from String-formatted to Object-formatted if needed
  // (we check in cache first)
  options = typeof options === "string" ?
    createOptions( options ) :
    jQuery.extend( {}, options );

  var // Flag to know if list is currently firing
    firing,

    // Last fire value for non-forgettable lists
    memory,

    // Flag to know if list was already fired
    fired,

    // Flag to prevent firing
    locked,

    // Actual callback list
    list = [],

    // Queue of execution data for repeatable lists
    queue = [],

    // Index of currently firing callback (modified by add/remove as needed)
    firingIndex = -1,

    // Fire callbacks
    fire = function() {

      // Enforce single-firing
      locked = options.once;

      // Execute callbacks for all pending executions,
      // respecting firingIndex overrides and runtime changes
      fired = firing = true;
      for ( ; queue.length; firingIndex = -1 ) {
        memory = queue.shift();
        while ( ++firingIndex < list.length ) {

          // Run callback and check for early termination
          if ( list[ firingIndex ].apply( memory[ 0 ], memory[ 1 ] ) === false &&
            options.stopOnFalse ) {

            // Jump to end and forget the data so .add doesn't re-fire
            firingIndex = list.length;
            memory = false;
          }
        }
      }

      // Forget the data if we're done with it
      if ( !options.memory ) {
        memory = false;
      }

      firing = false;

      // Clean up if we're done firing for good
      if ( locked ) {

        // Keep an empty list if we have data for future add calls
        if ( memory ) {
          list = [];

        // Otherwise, this object is spent
        } else {
          list = "";
        }
      }
    },

    // Actual Callbacks object
    self = {

      // Add a callback or a collection of callbacks to the list
      add: function() {
        if ( list ) {

          // If we have memory from a past run, we should fire after adding
          if ( memory && !firing ) {
            firingIndex = list.length - 1;
            queue.push( memory );
          }

          ( function add( args ) {
            jQuery.each( args, function( _, arg ) {
              if ( jQuery.isFunction( arg ) ) {
                if ( !options.unique || !self.has( arg ) ) {
                  list.push( arg );
                }
              } else if ( arg && arg.length && jQuery.type( arg ) !== "string" ) {

                // Inspect recursively
                add( arg );
              }
            } );
          } )( arguments );

          if ( memory && !firing ) {
            fire();
          }
        }
        return this;
      },

      // Remove a callback from the list
      remove: function() {
        jQuery.each( arguments, function( _, arg ) {
          var index;
          while ( ( index = jQuery.inArray( arg, list, index ) ) > -1 ) {
            list.splice( index, 1 );

            // Handle firing indexes
            if ( index <= firingIndex ) {
              firingIndex--;
            }
          }
        } );
        return this;
      },

      // Check if a given callback is in the list.
      // If no argument is given, return whether or not list has callbacks attached.
      has: function( fn ) {
        return fn ?
          jQuery.inArray( fn, list ) > -1 :
          list.length > 0;
      },

      // Remove all callbacks from the list
      empty: function() {
        if ( list ) {
          list = [];
        }
        return this;
      },

      // Disable .fire and .add
      // Abort any current/pending executions
      // Clear all callbacks and values
      disable: function() {
        locked = queue = [];
        list = memory = "";
        return this;
      },
      disabled: function() {
        return !list;
      },

      // Disable .fire
      // Also disable .add unless we have memory (since it would have no effect)
      // Abort any pending executions
      lock: function() {
        locked = queue = [];
        if ( !memory ) {
          list = memory = "";
        }
        return this;
      },
      locked: function() {
        return !!locked;
      },

      // Call all callbacks with the given context and arguments
      fireWith: function( context, args ) {
        if ( !locked ) {
          args = args || [];
          args = [ context, args.slice ? args.slice() : args ];
          queue.push( args );
          if ( !firing ) {
            fire();
          }
        }
        return this;
      },

      // Call all the callbacks with the given arguments
      fire: function() {
        self.fireWith( this, arguments );
        return this;
      },

      // To know if the callbacks have already been called at least once
      fired: function() {
        return !!fired;
      }
    };

  return self;
};


jQuery.extend( {

  Deferred: function( func ) {
    var tuples = [

        // action, add listener, listener list, final state
        [ "resolve", "done", jQuery.Callbacks( "once memory" ), "resolved" ],
        [ "reject", "fail", jQuery.Callbacks( "once memory" ), "rejected" ],
        [ "notify", "progress", jQuery.Callbacks( "memory" ) ]
      ],
      state = "pending",
      promise = {
        state: function() {
          return state;
        },
        always: function() {
          deferred.done( arguments ).fail( arguments );
          return this;
        },
        then: function( /* fnDone, fnFail, fnProgress */ ) {
          var fns = arguments;
          return jQuery.Deferred( function( newDefer ) {
            jQuery.each( tuples, function( i, tuple ) {
              var fn = jQuery.isFunction( fns[ i ] ) && fns[ i ];

              // deferred[ done | fail | progress ] for forwarding actions to newDefer
              deferred[ tuple[ 1 ] ]( function() {
                var returned = fn && fn.apply( this, arguments );
                if ( returned && jQuery.isFunction( returned.promise ) ) {
                  returned.promise()
                    .progress( newDefer.notify )
                    .done( newDefer.resolve )
                    .fail( newDefer.reject );
                } else {
                  newDefer[ tuple[ 0 ] + "With" ](
                    this === promise ? newDefer.promise() : this,
                    fn ? [ returned ] : arguments
                  );
                }
              } );
            } );
            fns = null;
          } ).promise();
        },

        // Get a promise for this deferred
        // If obj is provided, the promise aspect is added to the object
        promise: function( obj ) {
          return obj != null ? jQuery.extend( obj, promise ) : promise;
        }
      },
      deferred = {};

    // Keep pipe for back-compat
    promise.pipe = promise.then;

    // Add list-specific methods
    jQuery.each( tuples, function( i, tuple ) {
      var list = tuple[ 2 ],
        stateString = tuple[ 3 ];

      // promise[ done | fail | progress ] = list.add
      promise[ tuple[ 1 ] ] = list.add;

      // Handle state
      if ( stateString ) {
        list.add( function() {

          // state = [ resolved | rejected ]
          state = stateString;

        // [ reject_list | resolve_list ].disable; progress_list.lock
        }, tuples[ i ^ 1 ][ 2 ].disable, tuples[ 2 ][ 2 ].lock );
      }

      // deferred[ resolve | reject | notify ]
      deferred[ tuple[ 0 ] ] = function() {
        deferred[ tuple[ 0 ] + "With" ]( this === deferred ? promise : this, arguments );
        return this;
      };
      deferred[ tuple[ 0 ] + "With" ] = list.fireWith;
    } );

    // Make the deferred a promise
    promise.promise( deferred );

    // Call given func if any
    if ( func ) {
      func.call( deferred, deferred );
    }

    // All done!
    return deferred;
  },

  // Deferred helper
  when: function( subordinate /* , ..., subordinateN */ ) {
    var i = 0,
      resolveValues = slice.call( arguments ),
      length = resolveValues.length,

      // the count of uncompleted subordinates
      remaining = length !== 1 ||
        ( subordinate && jQuery.isFunction( subordinate.promise ) ) ? length : 0,

      // the master Deferred.
      // If resolveValues consist of only a single Deferred, just use that.
      deferred = remaining === 1 ? subordinate : jQuery.Deferred(),

      // Update function for both resolve and progress values
      updateFunc = function( i, contexts, values ) {
        return function( value ) {
          contexts[ i ] = this;
          values[ i ] = arguments.length > 1 ? slice.call( arguments ) : value;
          if ( values === progressValues ) {
            deferred.notifyWith( contexts, values );
          } else if ( !( --remaining ) ) {
            deferred.resolveWith( contexts, values );
          }
        };
      },

      progressValues, progressContexts, resolveContexts;

    // Add listeners to Deferred subordinates; treat others as resolved
    if ( length > 1 ) {
      progressValues = new Array( length );
      progressContexts = new Array( length );
      resolveContexts = new Array( length );
      for ( ; i < length; i++ ) {
        if ( resolveValues[ i ] && jQuery.isFunction( resolveValues[ i ].promise ) ) {
          resolveValues[ i ].promise()
            .progress( updateFunc( i, progressContexts, progressValues ) )
            .done( updateFunc( i, resolveContexts, resolveValues ) )
            .fail( deferred.reject );
        } else {
          --remaining;
        }
      }
    }

    // If we're not waiting on anything, resolve the master
    if ( !remaining ) {
      deferred.resolveWith( resolveContexts, resolveValues );
    }

    return deferred.promise();
  }
} );


// The deferred used on DOM ready
var readyList;

jQuery.fn.ready = function( fn ) {

  // Add the callback
  jQuery.ready.promise().done( fn );

  return this;
};

jQuery.extend( {

  // Is the DOM ready to be used? Set to true once it occurs.
  isReady: false,

  // A counter to track how many items to wait for before
  // the ready event fires. See #6781
  readyWait: 1,

  // Hold (or release) the ready event
  holdReady: function( hold ) {
    if ( hold ) {
      jQuery.readyWait++;
    } else {
      jQuery.ready( true );
    }
  },

  // Handle when the DOM is ready
  ready: function( wait ) {

    // Abort if there are pending holds or we're already ready
    if ( wait === true ? --jQuery.readyWait : jQuery.isReady ) {
      return;
    }

    // Remember that the DOM is ready
    jQuery.isReady = true;

    // If a normal DOM Ready event fired, decrement, and wait if need be
    if ( wait !== true && --jQuery.readyWait > 0 ) {
      return;
    }

    // If there are functions bound, to execute
    readyList.resolveWith( document, [ jQuery ] );

    // Trigger any bound ready events
    if ( jQuery.fn.triggerHandler ) {
      jQuery( document ).triggerHandler( "ready" );
      jQuery( document ).off( "ready" );
    }
  }
} );

/**
 * The ready event handler and self cleanup method
 */
function completed() {
  document.removeEventListener( "DOMContentLoaded", completed );
  window.removeEventListener( "load", completed );
  jQuery.ready();
}

jQuery.ready.promise = function( obj ) {
  if ( !readyList ) {

    readyList = jQuery.Deferred();

    // Catch cases where $(document).ready() is called
    // after the browser event has already occurred.
    // Support: IE9-10 only
    // Older IE sometimes signals "interactive" too soon
    if ( document.readyState === "complete" ||
      ( document.readyState !== "loading" && !document.documentElement.doScroll ) ) {

      // Handle it asynchronously to allow scripts the opportunity to delay ready
      window.setTimeout( jQuery.ready );

    } else {

      // Use the handy event callback
      document.addEventListener( "DOMContentLoaded", completed );

      // A fallback to window.onload, that will always work
      window.addEventListener( "load", completed );
    }
  }
  return readyList.promise( obj );
};

// Kick off the DOM ready check even if the user does not
jQuery.ready.promise();




// Multifunctional method to get and set values of a collection
// The value/s can optionally be executed if it's a function
var access = function( elems, fn, key, value, chainable, emptyGet, raw ) {
  var i = 0,
    len = elems.length,
    bulk = key == null;

  // Sets many values
  if ( jQuery.type( key ) === "object" ) {
    chainable = true;
    for ( i in key ) {
      access( elems, fn, i, key[ i ], true, emptyGet, raw );
    }

  // Sets one value
  } else if ( value !== undefined ) {
    chainable = true;

    if ( !jQuery.isFunction( value ) ) {
      raw = true;
    }

    if ( bulk ) {

      // Bulk operations run against the entire set
      if ( raw ) {
        fn.call( elems, value );
        fn = null;

      // ...except when executing function values
      } else {
        bulk = fn;
        fn = function( elem, key, value ) {
          return bulk.call( jQuery( elem ), value );
        };
      }
    }

    if ( fn ) {
      for ( ; i < len; i++ ) {
        fn(
          elems[ i ], key, raw ?
          value :
          value.call( elems[ i ], i, fn( elems[ i ], key ) )
        );
      }
    }
  }

  return chainable ?
    elems :

    // Gets
    bulk ?
      fn.call( elems ) :
      len ? fn( elems[ 0 ], key ) : emptyGet;
};
var acceptData = function( owner ) {

  // Accepts only:
  //  - Node
  //    - Node.ELEMENT_NODE
  //    - Node.DOCUMENT_NODE
  //  - Object
  //    - Any
  /* jshint -W018 */
  return owner.nodeType === 1 || owner.nodeType === 9 || !( +owner.nodeType );
};




function Data() {
  this.expando = jQuery.expando + Data.uid++;
}

Data.uid = 1;

Data.prototype = {

  register: function( owner, initial ) {
    var value = initial || {};

    // If it is a node unlikely to be stringify-ed or looped over
    // use plain assignment
    if ( owner.nodeType ) {
      owner[ this.expando ] = value;

    // Otherwise secure it in a non-enumerable, non-writable property
    // configurability must be true to allow the property to be
    // deleted with the delete operator
    } else {
      Object.defineProperty( owner, this.expando, {
        value: value,
        writable: true,
        configurable: true
      } );
    }
    return owner[ this.expando ];
  },
  cache: function( owner ) {

    // We can accept data for non-element nodes in modern browsers,
    // but we should not, see #8335.
    // Always return an empty object.
    if ( !acceptData( owner ) ) {
      return {};
    }

    // Check if the owner object already has a cache
    var value = owner[ this.expando ];

    // If not, create one
    if ( !value ) {
      value = {};

      // We can accept data for non-element nodes in modern browsers,
      // but we should not, see #8335.
      // Always return an empty object.
      if ( acceptData( owner ) ) {

        // If it is a node unlikely to be stringify-ed or looped over
        // use plain assignment
        if ( owner.nodeType ) {
          owner[ this.expando ] = value;

        // Otherwise secure it in a non-enumerable property
        // configurable must be true to allow the property to be
        // deleted when data is removed
        } else {
          Object.defineProperty( owner, this.expando, {
            value: value,
            configurable: true
          } );
        }
      }
    }

    return value;
  },
  set: function( owner, data, value ) {
    var prop,
      cache = this.cache( owner );

    // Handle: [ owner, key, value ] args
    if ( typeof data === "string" ) {
      cache[ data ] = value;

    // Handle: [ owner, { properties } ] args
    } else {

      // Copy the properties one-by-one to the cache object
      for ( prop in data ) {
        cache[ prop ] = data[ prop ];
      }
    }
    return cache;
  },
  get: function( owner, key ) {
    return key === undefined ?
      this.cache( owner ) :
      owner[ this.expando ] && owner[ this.expando ][ key ];
  },
  access: function( owner, key, value ) {
    var stored;

    // In cases where either:
    //
    //   1. No key was specified
    //   2. A string key was specified, but no value provided
    //
    // Take the "read" path and allow the get method to determine
    // which value to return, respectively either:
    //
    //   1. The entire cache object
    //   2. The data stored at the key
    //
    if ( key === undefined ||
        ( ( key && typeof key === "string" ) && value === undefined ) ) {

      stored = this.get( owner, key );

      return stored !== undefined ?
        stored : this.get( owner, jQuery.camelCase( key ) );
    }

    // When the key is not a string, or both a key and value
    // are specified, set or extend (existing objects) with either:
    //
    //   1. An object of properties
    //   2. A key and value
    //
    this.set( owner, key, value );

    // Since the "set" path can have two possible entry points
    // return the expected data based on which path was taken[*]
    return value !== undefined ? value : key;
  },
  remove: function( owner, key ) {
    var i, name, camel,
      cache = owner[ this.expando ];

    if ( cache === undefined ) {
      return;
    }

    if ( key === undefined ) {
      this.register( owner );

    } else {

      // Support array or space separated string of keys
      if ( jQuery.isArray( key ) ) {

        // If "name" is an array of keys...
        // When data is initially created, via ("key", "val") signature,
        // keys will be converted to camelCase.
        // Since there is no way to tell _how_ a key was added, remove
        // both plain key and camelCase key. #12786
        // This will only penalize the array argument path.
        name = key.concat( key.map( jQuery.camelCase ) );
      } else {
        camel = jQuery.camelCase( key );

        // Try the string as a key before any manipulation
        if ( key in cache ) {
          name = [ key, camel ];
        } else {

          // If a key with the spaces exists, use it.
          // Otherwise, create an array by matching non-whitespace
          name = camel;
          name = name in cache ?
            [ name ] : ( name.match( rnotwhite ) || [] );
        }
      }

      i = name.length;

      while ( i-- ) {
        delete cache[ name[ i ] ];
      }
    }

    // Remove the expando if there's no more data
    if ( key === undefined || jQuery.isEmptyObject( cache ) ) {

      // Support: Chrome <= 35-45+
      // Webkit & Blink performance suffers when deleting properties
      // from DOM nodes, so set to undefined instead
      // https://code.google.com/p/chromium/issues/detail?id=378607
      if ( owner.nodeType ) {
        owner[ this.expando ] = undefined;
      } else {
        delete owner[ this.expando ];
      }
    }
  },
  hasData: function( owner ) {
    var cache = owner[ this.expando ];
    return cache !== undefined && !jQuery.isEmptyObject( cache );
  }
};
var dataPriv = new Data();

var dataUser = new Data();



//  Implementation Summary
//
//  1. Enforce API surface and semantic compatibility with 1.9.x branch
//  2. Improve the module's maintainability by reducing the storage
//    paths to a single mechanism.
//  3. Use the same single mechanism to support "private" and "user" data.
//  4. _Never_ expose "private" data to user code (TODO: Drop _data, _removeData)
//  5. Avoid exposing implementation details on user objects (eg. expando properties)
//  6. Provide a clear path for implementation upgrade to WeakMap in 2014

var rbrace = /^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,
  rmultiDash = /[A-Z]/g;

function dataAttr( elem, key, data ) {
  var name;

  // If nothing was found internally, try to fetch any
  // data from the HTML5 data-* attribute
  if ( data === undefined && elem.nodeType === 1 ) {
    name = "data-" + key.replace( rmultiDash, "-$&" ).toLowerCase();
    data = elem.getAttribute( name );

    if ( typeof data === "string" ) {
      try {
        data = data === "true" ? true :
          data === "false" ? false :
          data === "null" ? null :

          // Only convert to a number if it doesn't change the string
          +data + "" === data ? +data :
          rbrace.test( data ) ? jQuery.parseJSON( data ) :
          data;
      } catch ( e ) {}

      // Make sure we set the data so it isn't changed later
      dataUser.set( elem, key, data );
    } else {
      data = undefined;
    }
  }
  return data;
}

jQuery.extend( {
  hasData: function( elem ) {
    return dataUser.hasData( elem ) || dataPriv.hasData( elem );
  },

  data: function( elem, name, data ) {
    return dataUser.access( elem, name, data );
  },

  removeData: function( elem, name ) {
    dataUser.remove( elem, name );
  },

  // TODO: Now that all calls to _data and _removeData have been replaced
  // with direct calls to dataPriv methods, these can be deprecated.
  _data: function( elem, name, data ) {
    return dataPriv.access( elem, name, data );
  },

  _removeData: function( elem, name ) {
    dataPriv.remove( elem, name );
  }
} );

jQuery.fn.extend( {
  data: function( key, value ) {
    var i, name, data,
      elem = this[ 0 ],
      attrs = elem && elem.attributes;

    // Gets all values
    if ( key === undefined ) {
      if ( this.length ) {
        data = dataUser.get( elem );

        if ( elem.nodeType === 1 && !dataPriv.get( elem, "hasDataAttrs" ) ) {
          i = attrs.length;
          while ( i-- ) {

            // Support: IE11+
            // The attrs elements can be null (#14894)
            if ( attrs[ i ] ) {
              name = attrs[ i ].name;
              if ( name.indexOf( "data-" ) === 0 ) {
                name = jQuery.camelCase( name.slice( 5 ) );
                dataAttr( elem, name, data[ name ] );
              }
            }
          }
          dataPriv.set( elem, "hasDataAttrs", true );
        }
      }

      return data;
    }

    // Sets multiple values
    if ( typeof key === "object" ) {
      return this.each( function() {
        dataUser.set( this, key );
      } );
    }

    return access( this, function( value ) {
      var data, camelKey;

      // The calling jQuery object (element matches) is not empty
      // (and therefore has an element appears at this[ 0 ]) and the
      // `value` parameter was not undefined. An empty jQuery object
      // will result in `undefined` for elem = this[ 0 ] which will
      // throw an exception if an attempt to read a data cache is made.
      if ( elem && value === undefined ) {

        // Attempt to get data from the cache
        // with the key as-is
        data = dataUser.get( elem, key ) ||

          // Try to find dashed key if it exists (gh-2779)
          // This is for 2.2.x only
          dataUser.get( elem, key.replace( rmultiDash, "-$&" ).toLowerCase() );

        if ( data !== undefined ) {
          return data;
        }

        camelKey = jQuery.camelCase( key );

        // Attempt to get data from the cache
        // with the key camelized
        data = dataUser.get( elem, camelKey );
        if ( data !== undefined ) {
          return data;
        }

        // Attempt to "discover" the data in
        // HTML5 custom data-* attrs
        data = dataAttr( elem, camelKey, undefined );
        if ( data !== undefined ) {
          return data;
        }

        // We tried really hard, but the data doesn't exist.
        return;
      }

      // Set the data...
      camelKey = jQuery.camelCase( key );
      this.each( function() {

        // First, attempt to store a copy or reference of any
        // data that might've been store with a camelCased key.
        var data = dataUser.get( this, camelKey );

        // For HTML5 data-* attribute interop, we have to
        // store property names with dashes in a camelCase form.
        // This might not apply to all properties...*
        dataUser.set( this, camelKey, value );

        // *... In the case of properties that might _actually_
        // have dashes, we need to also store a copy of that
        // unchanged property.
        if ( key.indexOf( "-" ) > -1 && data !== undefined ) {
          dataUser.set( this, key, value );
        }
      } );
    }, null, value, arguments.length > 1, null, true );
  },

  removeData: function( key ) {
    return this.each( function() {
      dataUser.remove( this, key );
    } );
  }
} );


jQuery.extend( {
  queue: function( elem, type, data ) {
    var queue;

    if ( elem ) {
      type = ( type || "fx" ) + "queue";
      queue = dataPriv.get( elem, type );

      // Speed up dequeue by getting out quickly if this is just a lookup
      if ( data ) {
        if ( !queue || jQuery.isArray( data ) ) {
          queue = dataPriv.access( elem, type, jQuery.makeArray( data ) );
        } else {
          queue.push( data );
        }
      }
      return queue || [];
    }
  },

  dequeue: function( elem, type ) {
    type = type || "fx";

    var queue = jQuery.queue( elem, type ),
      startLength = queue.length,
      fn = queue.shift(),
      hooks = jQuery._queueHooks( elem, type ),
      next = function() {
        jQuery.dequeue( elem, type );
      };

    // If the fx queue is dequeued, always remove the progress sentinel
    if ( fn === "inprogress" ) {
      fn = queue.shift();
      startLength--;
    }

    if ( fn ) {

      // Add a progress sentinel to prevent the fx queue from being
      // automatically dequeued
      if ( type === "fx" ) {
        queue.unshift( "inprogress" );
      }

      // Clear up the last queue stop function
      delete hooks.stop;
      fn.call( elem, next, hooks );
    }

    if ( !startLength && hooks ) {
      hooks.empty.fire();
    }
  },

  // Not public - generate a queueHooks object, or return the current one
  _queueHooks: function( elem, type ) {
    var key = type + "queueHooks";
    return dataPriv.get( elem, key ) || dataPriv.access( elem, key, {
      empty: jQuery.Callbacks( "once memory" ).add( function() {
        dataPriv.remove( elem, [ type + "queue", key ] );
      } )
    } );
  }
} );

jQuery.fn.extend( {
  queue: function( type, data ) {
    var setter = 2;

    if ( typeof type !== "string" ) {
      data = type;
      type = "fx";
      setter--;
    }

    if ( arguments.length < setter ) {
      return jQuery.queue( this[ 0 ], type );
    }

    return data === undefined ?
      this :
      this.each( function() {
        var queue = jQuery.queue( this, type, data );

        // Ensure a hooks for this queue
        jQuery._queueHooks( this, type );

        if ( type === "fx" && queue[ 0 ] !== "inprogress" ) {
          jQuery.dequeue( this, type );
        }
      } );
  },
  dequeue: function( type ) {
    return this.each( function() {
      jQuery.dequeue( this, type );
    } );
  },
  clearQueue: function( type ) {
    return this.queue( type || "fx", [] );
  },

  // Get a promise resolved when queues of a certain type
  // are emptied (fx is the type by default)
  promise: function( type, obj ) {
    var tmp,
      count = 1,
      defer = jQuery.Deferred(),
      elements = this,
      i = this.length,
      resolve = function() {
        if ( !( --count ) ) {
          defer.resolveWith( elements, [ elements ] );
        }
      };

    if ( typeof type !== "string" ) {
      obj = type;
      type = undefined;
    }
    type = type || "fx";

    while ( i-- ) {
      tmp = dataPriv.get( elements[ i ], type + "queueHooks" );
      if ( tmp && tmp.empty ) {
        count++;
        tmp.empty.add( resolve );
      }
    }
    resolve();
    return defer.promise( obj );
  }
} );
var pnum = ( /[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/ ).source;

var rcssNum = new RegExp( "^(?:([+-])=|)(" + pnum + ")([a-z%]*)$", "i" );


var cssExpand = [ "Top", "Right", "Bottom", "Left" ];

var isHidden = function( elem, el ) {

    // isHidden might be called from jQuery#filter function;
    // in that case, element will be second argument
    elem = el || elem;
    return jQuery.css( elem, "display" ) === "none" ||
      !jQuery.contains( elem.ownerDocument, elem );
  };



function adjustCSS( elem, prop, valueParts, tween ) {
  var adjusted,
    scale = 1,
    maxIterations = 20,
    currentValue = tween ?
      function() { return tween.cur(); } :
      function() { return jQuery.css( elem, prop, "" ); },
    initial = currentValue(),
    unit = valueParts && valueParts[ 3 ] || ( jQuery.cssNumber[ prop ] ? "" : "px" ),

    // Starting value computation is required for potential unit mismatches
    initialInUnit = ( jQuery.cssNumber[ prop ] || unit !== "px" && +initial ) &&
      rcssNum.exec( jQuery.css( elem, prop ) );

  if ( initialInUnit && initialInUnit[ 3 ] !== unit ) {

    // Trust units reported by jQuery.css
    unit = unit || initialInUnit[ 3 ];

    // Make sure we update the tween properties later on
    valueParts = valueParts || [];

    // Iteratively approximate from a nonzero starting point
    initialInUnit = +initial || 1;

    do {

      // If previous iteration zeroed out, double until we get *something*.
      // Use string for doubling so we don't accidentally see scale as unchanged below
      scale = scale || ".5";

      // Adjust and apply
      initialInUnit = initialInUnit / scale;
      jQuery.style( elem, prop, initialInUnit + unit );

    // Update scale, tolerating zero or NaN from tween.cur()
    // Break the loop if scale is unchanged or perfect, or if we've just had enough.
    } while (
      scale !== ( scale = currentValue() / initial ) && scale !== 1 && --maxIterations
    );
  }

  if ( valueParts ) {
    initialInUnit = +initialInUnit || +initial || 0;

    // Apply relative offset (+=/-=) if specified
    adjusted = valueParts[ 1 ] ?
      initialInUnit + ( valueParts[ 1 ] + 1 ) * valueParts[ 2 ] :
      +valueParts[ 2 ];
    if ( tween ) {
      tween.unit = unit;
      tween.start = initialInUnit;
      tween.end = adjusted;
    }
  }
  return adjusted;
}
var rcheckableType = ( /^(?:checkbox|radio)$/i );

var rtagName = ( /<([\w:-]+)/ );

var rscriptType = ( /^$|\/(?:java|ecma)script/i );



// We have to close these tags to support XHTML (#13200)
var wrapMap = {

  // Support: IE9
  option: [ 1, "<select multiple='multiple'>", "</select>" ],

  // XHTML parsers do not magically insert elements in the
  // same way that tag soup parsers do. So we cannot shorten
  // this by omitting <tbody> or other required elements.
  thead: [ 1, "<table>", "</table>" ],
  col: [ 2, "<table><colgroup>", "</colgroup></table>" ],
  tr: [ 2, "<table><tbody>", "</tbody></table>" ],
  td: [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ],

  _default: [ 0, "", "" ]
};

// Support: IE9
wrapMap.optgroup = wrapMap.option;

wrapMap.tbody = wrapMap.tfoot = wrapMap.colgroup = wrapMap.caption = wrapMap.thead;
wrapMap.th = wrapMap.td;


function getAll( context, tag ) {

  // Support: IE9-11+
  // Use typeof to avoid zero-argument method invocation on host objects (#15151)
  var ret = typeof context.getElementsByTagName !== "undefined" ?
      context.getElementsByTagName( tag || "*" ) :
      typeof context.querySelectorAll !== "undefined" ?
        context.querySelectorAll( tag || "*" ) :
      [];

  return tag === undefined || tag && jQuery.nodeName( context, tag ) ?
    jQuery.merge( [ context ], ret ) :
    ret;
}


// Mark scripts as having already been evaluated
function setGlobalEval( elems, refElements ) {
  var i = 0,
    l = elems.length;

  for ( ; i < l; i++ ) {
    dataPriv.set(
      elems[ i ],
      "globalEval",
      !refElements || dataPriv.get( refElements[ i ], "globalEval" )
    );
  }
}


var rhtml = /<|&#?\w+;/;

function buildFragment( elems, context, scripts, selection, ignored ) {
  var elem, tmp, tag, wrap, contains, j,
    fragment = context.createDocumentFragment(),
    nodes = [],
    i = 0,
    l = elems.length;

  for ( ; i < l; i++ ) {
    elem = elems[ i ];

    if ( elem || elem === 0 ) {

      // Add nodes directly
      if ( jQuery.type( elem ) === "object" ) {

        // Support: Android<4.1, PhantomJS<2
        // push.apply(_, arraylike) throws on ancient WebKit
        jQuery.merge( nodes, elem.nodeType ? [ elem ] : elem );

      // Convert non-html into a text node
      } else if ( !rhtml.test( elem ) ) {
        nodes.push( context.createTextNode( elem ) );

      // Convert html into DOM nodes
      } else {
        tmp = tmp || fragment.appendChild( context.createElement( "div" ) );

        // Deserialize a standard representation
        tag = ( rtagName.exec( elem ) || [ "", "" ] )[ 1 ].toLowerCase();
        wrap = wrapMap[ tag ] || wrapMap._default;
        tmp.innerHTML = wrap[ 1 ] + jQuery.htmlPrefilter( elem ) + wrap[ 2 ];

        // Descend through wrappers to the right content
        j = wrap[ 0 ];
        while ( j-- ) {
          tmp = tmp.lastChild;
        }

        // Support: Android<4.1, PhantomJS<2
        // push.apply(_, arraylike) throws on ancient WebKit
        jQuery.merge( nodes, tmp.childNodes );

        // Remember the top-level container
        tmp = fragment.firstChild;

        // Ensure the created nodes are orphaned (#12392)
        tmp.textContent = "";
      }
    }
  }

  // Remove wrapper from fragment
  fragment.textContent = "";

  i = 0;
  while ( ( elem = nodes[ i++ ] ) ) {

    // Skip elements already in the context collection (trac-4087)
    if ( selection && jQuery.inArray( elem, selection ) > -1 ) {
      if ( ignored ) {
        ignored.push( elem );
      }
      continue;
    }

    contains = jQuery.contains( elem.ownerDocument, elem );

    // Append to fragment
    tmp = getAll( fragment.appendChild( elem ), "script" );

    // Preserve script evaluation history
    if ( contains ) {
      setGlobalEval( tmp );
    }

    // Capture executables
    if ( scripts ) {
      j = 0;
      while ( ( elem = tmp[ j++ ] ) ) {
        if ( rscriptType.test( elem.type || "" ) ) {
          scripts.push( elem );
        }
      }
    }
  }

  return fragment;
}


( function() {
  var fragment = document.createDocumentFragment(),
    div = fragment.appendChild( document.createElement( "div" ) ),
    input = document.createElement( "input" );

  // Support: Android 4.0-4.3, Safari<=5.1
  // Check state lost if the name is set (#11217)
  // Support: Windows Web Apps (WWA)
  // `name` and `type` must use .setAttribute for WWA (#14901)
  input.setAttribute( "type", "radio" );
  input.setAttribute( "checked", "checked" );
  input.setAttribute( "name", "t" );

  div.appendChild( input );

  // Support: Safari<=5.1, Android<4.2
  // Older WebKit doesn't clone checked state correctly in fragments
  support.checkClone = div.cloneNode( true ).cloneNode( true ).lastChild.checked;

  // Support: IE<=11+
  // Make sure textarea (and checkbox) defaultValue is properly cloned
  div.innerHTML = "<textarea>x</textarea>";
  support.noCloneChecked = !!div.cloneNode( true ).lastChild.defaultValue;
} )();


var
  rkeyEvent = /^key/,
  rmouseEvent = /^(?:mouse|pointer|contextmenu|drag|drop)|click/,
  rtypenamespace = /^([^.]*)(?:\.(.+)|)/;

function returnTrue() {
  return true;
}

function returnFalse() {
  return false;
}

// Support: IE9
// See #13393 for more info
function safeActiveElement() {
  try {
    return document.activeElement;
  } catch ( err ) { }
}

function on( elem, types, selector, data, fn, one ) {
  var origFn, type;

  // Types can be a map of types/handlers
  if ( typeof types === "object" ) {

    // ( types-Object, selector, data )
    if ( typeof selector !== "string" ) {

      // ( types-Object, data )
      data = data || selector;
      selector = undefined;
    }
    for ( type in types ) {
      on( elem, type, selector, data, types[ type ], one );
    }
    return elem;
  }

  if ( data == null && fn == null ) {

    // ( types, fn )
    fn = selector;
    data = selector = undefined;
  } else if ( fn == null ) {
    if ( typeof selector === "string" ) {

      // ( types, selector, fn )
      fn = data;
      data = undefined;
    } else {

      // ( types, data, fn )
      fn = data;
      data = selector;
      selector = undefined;
    }
  }
  if ( fn === false ) {
    fn = returnFalse;
  } else if ( !fn ) {
    return elem;
  }

  if ( one === 1 ) {
    origFn = fn;
    fn = function( event ) {

      // Can use an empty set, since event contains the info
      jQuery().off( event );
      return origFn.apply( this, arguments );
    };

    // Use same guid so caller can remove using origFn
    fn.guid = origFn.guid || ( origFn.guid = jQuery.guid++ );
  }
  return elem.each( function() {
    jQuery.event.add( this, types, fn, data, selector );
  } );
}

/*
 * Helper functions for managing events -- not part of the public interface.
 * Props to Dean Edwards' addEvent library for many of the ideas.
 */
jQuery.event = {

  global: {},

  add: function( elem, types, handler, data, selector ) {

    var handleObjIn, eventHandle, tmp,
      events, t, handleObj,
      special, handlers, type, namespaces, origType,
      elemData = dataPriv.get( elem );

    // Don't attach events to noData or text/comment nodes (but allow plain objects)
    if ( !elemData ) {
      return;
    }

    // Caller can pass in an object of custom data in lieu of the handler
    if ( handler.handler ) {
      handleObjIn = handler;
      handler = handleObjIn.handler;
      selector = handleObjIn.selector;
    }

    // Make sure that the handler has a unique ID, used to find/remove it later
    if ( !handler.guid ) {
      handler.guid = jQuery.guid++;
    }

    // Init the element's event structure and main handler, if this is the first
    if ( !( events = elemData.events ) ) {
      events = elemData.events = {};
    }
    if ( !( eventHandle = elemData.handle ) ) {
      eventHandle = elemData.handle = function( e ) {

        // Discard the second event of a jQuery.event.trigger() and
        // when an event is called after a page has unloaded
        return typeof jQuery !== "undefined" && jQuery.event.triggered !== e.type ?
          jQuery.event.dispatch.apply( elem, arguments ) : undefined;
      };
    }

    // Handle multiple events separated by a space
    types = ( types || "" ).match( rnotwhite ) || [ "" ];
    t = types.length;
    while ( t-- ) {
      tmp = rtypenamespace.exec( types[ t ] ) || [];
      type = origType = tmp[ 1 ];
      namespaces = ( tmp[ 2 ] || "" ).split( "." ).sort();

      // There *must* be a type, no attaching namespace-only handlers
      if ( !type ) {
        continue;
      }

      // If event changes its type, use the special event handlers for the changed type
      special = jQuery.event.special[ type ] || {};

      // If selector defined, determine special event api type, otherwise given type
      type = ( selector ? special.delegateType : special.bindType ) || type;

      // Update special based on newly reset type
      special = jQuery.event.special[ type ] || {};

      // handleObj is passed to all event handlers
      handleObj = jQuery.extend( {
        type: type,
        origType: origType,
        data: data,
        handler: handler,
        guid: handler.guid,
        selector: selector,
        needsContext: selector && jQuery.expr.match.needsContext.test( selector ),
        namespace: namespaces.join( "." )
      }, handleObjIn );

      // Init the event handler queue if we're the first
      if ( !( handlers = events[ type ] ) ) {
        handlers = events[ type ] = [];
        handlers.delegateCount = 0;

        // Only use addEventListener if the special events handler returns false
        if ( !special.setup ||
          special.setup.call( elem, data, namespaces, eventHandle ) === false ) {

          if ( elem.addEventListener ) {
            elem.addEventListener( type, eventHandle );
          }
        }
      }

      if ( special.add ) {
        special.add.call( elem, handleObj );

        if ( !handleObj.handler.guid ) {
          handleObj.handler.guid = handler.guid;
        }
      }

      // Add to the element's handler list, delegates in front
      if ( selector ) {
        handlers.splice( handlers.delegateCount++, 0, handleObj );
      } else {
        handlers.push( handleObj );
      }

      // Keep track of which events have ever been used, for event optimization
      jQuery.event.global[ type ] = true;
    }

  },

  // Detach an event or set of events from an element
  remove: function( elem, types, handler, selector, mappedTypes ) {

    var j, origCount, tmp,
      events, t, handleObj,
      special, handlers, type, namespaces, origType,
      elemData = dataPriv.hasData( elem ) && dataPriv.get( elem );

    if ( !elemData || !( events = elemData.events ) ) {
      return;
    }

    // Once for each type.namespace in types; type may be omitted
    types = ( types || "" ).match( rnotwhite ) || [ "" ];
    t = types.length;
    while ( t-- ) {
      tmp = rtypenamespace.exec( types[ t ] ) || [];
      type = origType = tmp[ 1 ];
      namespaces = ( tmp[ 2 ] || "" ).split( "." ).sort();

      // Unbind all events (on this namespace, if provided) for the element
      if ( !type ) {
        for ( type in events ) {
          jQuery.event.remove( elem, type + types[ t ], handler, selector, true );
        }
        continue;
      }

      special = jQuery.event.special[ type ] || {};
      type = ( selector ? special.delegateType : special.bindType ) || type;
      handlers = events[ type ] || [];
      tmp = tmp[ 2 ] &&
        new RegExp( "(^|\\.)" + namespaces.join( "\\.(?:.*\\.|)" ) + "(\\.|$)" );

      // Remove matching events
      origCount = j = handlers.length;
      while ( j-- ) {
        handleObj = handlers[ j ];

        if ( ( mappedTypes || origType === handleObj.origType ) &&
          ( !handler || handler.guid === handleObj.guid ) &&
          ( !tmp || tmp.test( handleObj.namespace ) ) &&
          ( !selector || selector === handleObj.selector ||
            selector === "**" && handleObj.selector ) ) {
          handlers.splice( j, 1 );

          if ( handleObj.selector ) {
            handlers.delegateCount--;
          }
          if ( special.remove ) {
            special.remove.call( elem, handleObj );
          }
        }
      }

      // Remove generic event handler if we removed something and no more handlers exist
      // (avoids potential for endless recursion during removal of special event handlers)
      if ( origCount && !handlers.length ) {
        if ( !special.teardown ||
          special.teardown.call( elem, namespaces, elemData.handle ) === false ) {

          jQuery.removeEvent( elem, type, elemData.handle );
        }

        delete events[ type ];
      }
    }

    // Remove data and the expando if it's no longer used
    if ( jQuery.isEmptyObject( events ) ) {
      dataPriv.remove( elem, "handle events" );
    }
  },

  dispatch: function( event ) {

    // Make a writable jQuery.Event from the native event object
    event = jQuery.event.fix( event );

    var i, j, ret, matched, handleObj,
      handlerQueue = [],
      args = slice.call( arguments ),
      handlers = ( dataPriv.get( this, "events" ) || {} )[ event.type ] || [],
      special = jQuery.event.special[ event.type ] || {};

    // Use the fix-ed jQuery.Event rather than the (read-only) native event
    args[ 0 ] = event;
    event.delegateTarget = this;

    // Call the preDispatch hook for the mapped type, and let it bail if desired
    if ( special.preDispatch && special.preDispatch.call( this, event ) === false ) {
      return;
    }

    // Determine handlers
    handlerQueue = jQuery.event.handlers.call( this, event, handlers );

    // Run delegates first; they may want to stop propagation beneath us
    i = 0;
    while ( ( matched = handlerQueue[ i++ ] ) && !event.isPropagationStopped() ) {
      event.currentTarget = matched.elem;

      j = 0;
      while ( ( handleObj = matched.handlers[ j++ ] ) &&
        !event.isImmediatePropagationStopped() ) {

        // Triggered event must either 1) have no namespace, or 2) have namespace(s)
        // a subset or equal to those in the bound event (both can have no namespace).
        if ( !event.rnamespace || event.rnamespace.test( handleObj.namespace ) ) {

          event.handleObj = handleObj;
          event.data = handleObj.data;

          ret = ( ( jQuery.event.special[ handleObj.origType ] || {} ).handle ||
            handleObj.handler ).apply( matched.elem, args );

          if ( ret !== undefined ) {
            if ( ( event.result = ret ) === false ) {
              event.preventDefault();
              event.stopPropagation();
            }
          }
        }
      }
    }

    // Call the postDispatch hook for the mapped type
    if ( special.postDispatch ) {
      special.postDispatch.call( this, event );
    }

    return event.result;
  },

  handlers: function( event, handlers ) {
    var i, matches, sel, handleObj,
      handlerQueue = [],
      delegateCount = handlers.delegateCount,
      cur = event.target;

    // Support (at least): Chrome, IE9
    // Find delegate handlers
    // Black-hole SVG <use> instance trees (#13180)
    //
    // Support: Firefox<=42+
    // Avoid non-left-click in FF but don't block IE radio events (#3861, gh-2343)
    if ( delegateCount && cur.nodeType &&
      ( event.type !== "click" || isNaN( event.button ) || event.button < 1 ) ) {

      for ( ; cur !== this; cur = cur.parentNode || this ) {

        // Don't check non-elements (#13208)
        // Don't process clicks on disabled elements (#6911, #8165, #11382, #11764)
        if ( cur.nodeType === 1 && ( cur.disabled !== true || event.type !== "click" ) ) {
          matches = [];
          for ( i = 0; i < delegateCount; i++ ) {
            handleObj = handlers[ i ];

            // Don't conflict with Object.prototype properties (#13203)
            sel = handleObj.selector + " ";

            if ( matches[ sel ] === undefined ) {
              matches[ sel ] = handleObj.needsContext ?
                jQuery( sel, this ).index( cur ) > -1 :
                jQuery.find( sel, this, null, [ cur ] ).length;
            }
            if ( matches[ sel ] ) {
              matches.push( handleObj );
            }
          }
          if ( matches.length ) {
            handlerQueue.push( { elem: cur, handlers: matches } );
          }
        }
      }
    }

    // Add the remaining (directly-bound) handlers
    if ( delegateCount < handlers.length ) {
      handlerQueue.push( { elem: this, handlers: handlers.slice( delegateCount ) } );
    }

    return handlerQueue;
  },

  // Includes some event props shared by KeyEvent and MouseEvent
  props: ( "altKey bubbles cancelable ctrlKey currentTarget detail eventPhase " +
    "metaKey relatedTarget shiftKey target timeStamp view which" ).split( " " ),

  fixHooks: {},

  keyHooks: {
    props: "char charCode key keyCode".split( " " ),
    filter: function( event, original ) {

      // Add which for key events
      if ( event.which == null ) {
        event.which = original.charCode != null ? original.charCode : original.keyCode;
      }

      return event;
    }
  },

  mouseHooks: {
    props: ( "button buttons clientX clientY offsetX offsetY pageX pageY " +
      "screenX screenY toElement" ).split( " " ),
    filter: function( event, original ) {
      var eventDoc, doc, body,
        button = original.button;

      // Calculate pageX/Y if missing and clientX/Y available
      if ( event.pageX == null && original.clientX != null ) {
        eventDoc = event.target.ownerDocument || document;
        doc = eventDoc.documentElement;
        body = eventDoc.body;

        event.pageX = original.clientX +
          ( doc && doc.scrollLeft || body && body.scrollLeft || 0 ) -
          ( doc && doc.clientLeft || body && body.clientLeft || 0 );
        event.pageY = original.clientY +
          ( doc && doc.scrollTop  || body && body.scrollTop  || 0 ) -
          ( doc && doc.clientTop  || body && body.clientTop  || 0 );
      }

      // Add which for click: 1 === left; 2 === middle; 3 === right
      // Note: button is not normalized, so don't use it
      if ( !event.which && button !== undefined ) {
        event.which = ( button & 1 ? 1 : ( button & 2 ? 3 : ( button & 4 ? 2 : 0 ) ) );
      }

      return event;
    }
  },

  fix: function( event ) {
    if ( event[ jQuery.expando ] ) {
      return event;
    }

    // Create a writable copy of the event object and normalize some properties
    var i, prop, copy,
      type = event.type,
      originalEvent = event,
      fixHook = this.fixHooks[ type ];

    if ( !fixHook ) {
      this.fixHooks[ type ] = fixHook =
        rmouseEvent.test( type ) ? this.mouseHooks :
        rkeyEvent.test( type ) ? this.keyHooks :
        {};
    }
    copy = fixHook.props ? this.props.concat( fixHook.props ) : this.props;

    event = new jQuery.Event( originalEvent );

    i = copy.length;
    while ( i-- ) {
      prop = copy[ i ];
      event[ prop ] = originalEvent[ prop ];
    }

    // Support: Cordova 2.5 (WebKit) (#13255)
    // All events should have a target; Cordova deviceready doesn't
    if ( !event.target ) {
      event.target = document;
    }

    // Support: Safari 6.0+, Chrome<28
    // Target should not be a text node (#504, #13143)
    if ( event.target.nodeType === 3 ) {
      event.target = event.target.parentNode;
    }

    return fixHook.filter ? fixHook.filter( event, originalEvent ) : event;
  },

  special: {
    load: {

      // Prevent triggered image.load events from bubbling to window.load
      noBubble: true
    },
    focus: {

      // Fire native event if possible so blur/focus sequence is correct
      trigger: function() {
        if ( this !== safeActiveElement() && this.focus ) {
          this.focus();
          return false;
        }
      },
      delegateType: "focusin"
    },
    blur: {
      trigger: function() {
        if ( this === safeActiveElement() && this.blur ) {
          this.blur();
          return false;
        }
      },
      delegateType: "focusout"
    },
    click: {

      // For checkbox, fire native event so checked state will be right
      trigger: function() {
        if ( this.type === "checkbox" && this.click && jQuery.nodeName( this, "input" ) ) {
          this.click();
          return false;
        }
      },

      // For cross-browser consistency, don't fire native .click() on links
      _default: function( event ) {
        return jQuery.nodeName( event.target, "a" );
      }
    },

    beforeunload: {
      postDispatch: function( event ) {

        // Support: Firefox 20+
        // Firefox doesn't alert if the returnValue field is not set.
        if ( event.result !== undefined && event.originalEvent ) {
          event.originalEvent.returnValue = event.result;
        }
      }
    }
  }
};

jQuery.removeEvent = function( elem, type, handle ) {

  // This "if" is needed for plain objects
  if ( elem.removeEventListener ) {
    elem.removeEventListener( type, handle );
  }
};

jQuery.Event = function( src, props ) {

  // Allow instantiation without the 'new' keyword
  if ( !( this instanceof jQuery.Event ) ) {
    return new jQuery.Event( src, props );
  }

  // Event object
  if ( src && src.type ) {
    this.originalEvent = src;
    this.type = src.type;

    // Events bubbling up the document may have been marked as prevented
    // by a handler lower down the tree; reflect the correct value.
    this.isDefaultPrevented = src.defaultPrevented ||
        src.defaultPrevented === undefined &&

        // Support: Android<4.0
        src.returnValue === false ?
      returnTrue :
      returnFalse;

  // Event type
  } else {
    this.type = src;
  }

  // Put explicitly provided properties onto the event object
  if ( props ) {
    jQuery.extend( this, props );
  }

  // Create a timestamp if incoming event doesn't have one
  this.timeStamp = src && src.timeStamp || jQuery.now();

  // Mark it as fixed
  this[ jQuery.expando ] = true;
};

// jQuery.Event is based on DOM3 Events as specified by the ECMAScript Language Binding
// http://www.w3.org/TR/2003/WD-DOM-Level-3-Events-20030331/ecma-script-binding.html
jQuery.Event.prototype = {
  constructor: jQuery.Event,
  isDefaultPrevented: returnFalse,
  isPropagationStopped: returnFalse,
  isImmediatePropagationStopped: returnFalse,
  isSimulated: false,

  preventDefault: function() {
    var e = this.originalEvent;

    this.isDefaultPrevented = returnTrue;

    if ( e && !this.isSimulated ) {
      e.preventDefault();
    }
  },
  stopPropagation: function() {
    var e = this.originalEvent;

    this.isPropagationStopped = returnTrue;

    if ( e && !this.isSimulated ) {
      e.stopPropagation();
    }
  },
  stopImmediatePropagation: function() {
    var e = this.originalEvent;

    this.isImmediatePropagationStopped = returnTrue;

    if ( e && !this.isSimulated ) {
      e.stopImmediatePropagation();
    }

    this.stopPropagation();
  }
};

// Create mouseenter/leave events using mouseover/out and event-time checks
// so that event delegation works in jQuery.
// Do the same for pointerenter/pointerleave and pointerover/pointerout
//
// Support: Safari 7 only
// Safari sends mouseenter too often; see:
// https://code.google.com/p/chromium/issues/detail?id=470258
// for the description of the bug (it existed in older Chrome versions as well).
jQuery.each( {
  mouseenter: "mouseover",
  mouseleave: "mouseout",
  pointerenter: "pointerover",
  pointerleave: "pointerout"
}, function( orig, fix ) {
  jQuery.event.special[ orig ] = {
    delegateType: fix,
    bindType: fix,

    handle: function( event ) {
      var ret,
        target = this,
        related = event.relatedTarget,
        handleObj = event.handleObj;

      // For mouseenter/leave call the handler if related is outside the target.
      // NB: No relatedTarget if the mouse left/entered the browser window
      if ( !related || ( related !== target && !jQuery.contains( target, related ) ) ) {
        event.type = handleObj.origType;
        ret = handleObj.handler.apply( this, arguments );
        event.type = fix;
      }
      return ret;
    }
  };
} );

jQuery.fn.extend( {
  on: function( types, selector, data, fn ) {
    return on( this, types, selector, data, fn );
  },
  one: function( types, selector, data, fn ) {
    return on( this, types, selector, data, fn, 1 );
  },
  off: function( types, selector, fn ) {
    var handleObj, type;
    if ( types && types.preventDefault && types.handleObj ) {

      // ( event )  dispatched jQuery.Event
      handleObj = types.handleObj;
      jQuery( types.delegateTarget ).off(
        handleObj.namespace ?
          handleObj.origType + "." + handleObj.namespace :
          handleObj.origType,
        handleObj.selector,
        handleObj.handler
      );
      return this;
    }
    if ( typeof types === "object" ) {

      // ( types-object [, selector] )
      for ( type in types ) {
        this.off( type, selector, types[ type ] );
      }
      return this;
    }
    if ( selector === false || typeof selector === "function" ) {

      // ( types [, fn] )
      fn = selector;
      selector = undefined;
    }
    if ( fn === false ) {
      fn = returnFalse;
    }
    return this.each( function() {
      jQuery.event.remove( this, types, fn, selector );
    } );
  }
} );


var
  rxhtmlTag = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:-]+)[^>]*)\/>/gi,

  // Support: IE 10-11, Edge 10240+
  // In IE/Edge using regex groups here causes severe slowdowns.
  // See https://connect.microsoft.com/IE/feedback/details/1736512/
  rnoInnerhtml = /<script|<style|<link/i,

  // checked="checked" or checked
  rchecked = /checked\s*(?:[^=]|=\s*.checked.)/i,
  rscriptTypeMasked = /^true\/(.*)/,
  rcleanScript = /^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g;

// Manipulating tables requires a tbody
function manipulationTarget( elem, content ) {
  return jQuery.nodeName( elem, "table" ) &&
    jQuery.nodeName( content.nodeType !== 11 ? content : content.firstChild, "tr" ) ?

    elem.getElementsByTagName( "tbody" )[ 0 ] ||
      elem.appendChild( elem.ownerDocument.createElement( "tbody" ) ) :
    elem;
}

// Replace/restore the type attribute of script elements for safe DOM manipulation
function disableScript( elem ) {
  elem.type = ( elem.getAttribute( "type" ) !== null ) + "/" + elem.type;
  return elem;
}
function restoreScript( elem ) {
  var match = rscriptTypeMasked.exec( elem.type );

  if ( match ) {
    elem.type = match[ 1 ];
  } else {
    elem.removeAttribute( "type" );
  }

  return elem;
}

function cloneCopyEvent( src, dest ) {
  var i, l, type, pdataOld, pdataCur, udataOld, udataCur, events;

  if ( dest.nodeType !== 1 ) {
    return;
  }

  // 1. Copy private data: events, handlers, etc.
  if ( dataPriv.hasData( src ) ) {
    pdataOld = dataPriv.access( src );
    pdataCur = dataPriv.set( dest, pdataOld );
    events = pdataOld.events;

    if ( events ) {
      delete pdataCur.handle;
      pdataCur.events = {};

      for ( type in events ) {
        for ( i = 0, l = events[ type ].length; i < l; i++ ) {
          jQuery.event.add( dest, type, events[ type ][ i ] );
        }
      }
    }
  }

  // 2. Copy user data
  if ( dataUser.hasData( src ) ) {
    udataOld = dataUser.access( src );
    udataCur = jQuery.extend( {}, udataOld );

    dataUser.set( dest, udataCur );
  }
}

// Fix IE bugs, see support tests
function fixInput( src, dest ) {
  var nodeName = dest.nodeName.toLowerCase();

  // Fails to persist the checked state of a cloned checkbox or radio button.
  if ( nodeName === "input" && rcheckableType.test( src.type ) ) {
    dest.checked = src.checked;

  // Fails to return the selected option to the default selected state when cloning options
  } else if ( nodeName === "input" || nodeName === "textarea" ) {
    dest.defaultValue = src.defaultValue;
  }
}

function domManip( collection, args, callback, ignored ) {

  // Flatten any nested arrays
  args = concat.apply( [], args );

  var fragment, first, scripts, hasScripts, node, doc,
    i = 0,
    l = collection.length,
    iNoClone = l - 1,
    value = args[ 0 ],
    isFunction = jQuery.isFunction( value );

  // We can't cloneNode fragments that contain checked, in WebKit
  if ( isFunction ||
      ( l > 1 && typeof value === "string" &&
        !support.checkClone && rchecked.test( value ) ) ) {
    return collection.each( function( index ) {
      var self = collection.eq( index );
      if ( isFunction ) {
        args[ 0 ] = value.call( this, index, self.html() );
      }
      domManip( self, args, callback, ignored );
    } );
  }

  if ( l ) {
    fragment = buildFragment( args, collection[ 0 ].ownerDocument, false, collection, ignored );
    first = fragment.firstChild;

    if ( fragment.childNodes.length === 1 ) {
      fragment = first;
    }

    // Require either new content or an interest in ignored elements to invoke the callback
    if ( first || ignored ) {
      scripts = jQuery.map( getAll( fragment, "script" ), disableScript );
      hasScripts = scripts.length;

      // Use the original fragment for the last item
      // instead of the first because it can end up
      // being emptied incorrectly in certain situations (#8070).
      for ( ; i < l; i++ ) {
        node = fragment;

        if ( i !== iNoClone ) {
          node = jQuery.clone( node, true, true );

          // Keep references to cloned scripts for later restoration
          if ( hasScripts ) {

            // Support: Android<4.1, PhantomJS<2
            // push.apply(_, arraylike) throws on ancient WebKit
            jQuery.merge( scripts, getAll( node, "script" ) );
          }
        }

        callback.call( collection[ i ], node, i );
      }

      if ( hasScripts ) {
        doc = scripts[ scripts.length - 1 ].ownerDocument;

        // Reenable scripts
        jQuery.map( scripts, restoreScript );

        // Evaluate executable scripts on first document insertion
        for ( i = 0; i < hasScripts; i++ ) {
          node = scripts[ i ];
          if ( rscriptType.test( node.type || "" ) &&
            !dataPriv.access( node, "globalEval" ) &&
            jQuery.contains( doc, node ) ) {

            if ( node.src ) {

              // Optional AJAX dependency, but won't run scripts if not present
              if ( jQuery._evalUrl ) {
                jQuery._evalUrl( node.src );
              }
            } else {
              jQuery.globalEval( node.textContent.replace( rcleanScript, "" ) );
            }
          }
        }
      }
    }
  }

  return collection;
}

function remove( elem, selector, keepData ) {
  var node,
    nodes = selector ? jQuery.filter( selector, elem ) : elem,
    i = 0;

  for ( ; ( node = nodes[ i ] ) != null; i++ ) {
    if ( !keepData && node.nodeType === 1 ) {
      jQuery.cleanData( getAll( node ) );
    }

    if ( node.parentNode ) {
      if ( keepData && jQuery.contains( node.ownerDocument, node ) ) {
        setGlobalEval( getAll( node, "script" ) );
      }
      node.parentNode.removeChild( node );
    }
  }

  return elem;
}

jQuery.extend( {
  htmlPrefilter: function( html ) {
    return html.replace( rxhtmlTag, "<$1></$2>" );
  },

  clone: function( elem, dataAndEvents, deepDataAndEvents ) {
    var i, l, srcElements, destElements,
      clone = elem.cloneNode( true ),
      inPage = jQuery.contains( elem.ownerDocument, elem );

    // Fix IE cloning issues
    if ( !support.noCloneChecked && ( elem.nodeType === 1 || elem.nodeType === 11 ) &&
        !jQuery.isXMLDoc( elem ) ) {

      // We eschew Sizzle here for performance reasons: http://jsperf.com/getall-vs-sizzle/2
      destElements = getAll( clone );
      srcElements = getAll( elem );

      for ( i = 0, l = srcElements.length; i < l; i++ ) {
        fixInput( srcElements[ i ], destElements[ i ] );
      }
    }

    // Copy the events from the original to the clone
    if ( dataAndEvents ) {
      if ( deepDataAndEvents ) {
        srcElements = srcElements || getAll( elem );
        destElements = destElements || getAll( clone );

        for ( i = 0, l = srcElements.length; i < l; i++ ) {
          cloneCopyEvent( srcElements[ i ], destElements[ i ] );
        }
      } else {
        cloneCopyEvent( elem, clone );
      }
    }

    // Preserve script evaluation history
    destElements = getAll( clone, "script" );
    if ( destElements.length > 0 ) {
      setGlobalEval( destElements, !inPage && getAll( elem, "script" ) );
    }

    // Return the cloned set
    return clone;
  },

  cleanData: function( elems ) {
    var data, elem, type,
      special = jQuery.event.special,
      i = 0;

    for ( ; ( elem = elems[ i ] ) !== undefined; i++ ) {
      if ( acceptData( elem ) ) {
        if ( ( data = elem[ dataPriv.expando ] ) ) {
          if ( data.events ) {
            for ( type in data.events ) {
              if ( special[ type ] ) {
                jQuery.event.remove( elem, type );

              // This is a shortcut to avoid jQuery.event.remove's overhead
              } else {
                jQuery.removeEvent( elem, type, data.handle );
              }
            }
          }

          // Support: Chrome <= 35-45+
          // Assign undefined instead of using delete, see Data#remove
          elem[ dataPriv.expando ] = undefined;
        }
        if ( elem[ dataUser.expando ] ) {

          // Support: Chrome <= 35-45+
          // Assign undefined instead of using delete, see Data#remove
          elem[ dataUser.expando ] = undefined;
        }
      }
    }
  }
} );

jQuery.fn.extend( {

  // Keep domManip exposed until 3.0 (gh-2225)
  domManip: domManip,

  detach: function( selector ) {
    return remove( this, selector, true );
  },

  remove: function( selector ) {
    return remove( this, selector );
  },

  text: function( value ) {
    return access( this, function( value ) {
      return value === undefined ?
        jQuery.text( this ) :
        this.empty().each( function() {
          if ( this.nodeType === 1 || this.nodeType === 11 || this.nodeType === 9 ) {
            this.textContent = value;
          }
        } );
    }, null, value, arguments.length );
  },

  append: function() {
    return domManip( this, arguments, function( elem ) {
      if ( this.nodeType === 1 || this.nodeType === 11 || this.nodeType === 9 ) {
        var target = manipulationTarget( this, elem );
        target.appendChild( elem );
      }
    } );
  },

  prepend: function() {
    return domManip( this, arguments, function( elem ) {
      if ( this.nodeType === 1 || this.nodeType === 11 || this.nodeType === 9 ) {
        var target = manipulationTarget( this, elem );
        target.insertBefore( elem, target.firstChild );
      }
    } );
  },

  before: function() {
    return domManip( this, arguments, function( elem ) {
      if ( this.parentNode ) {
        this.parentNode.insertBefore( elem, this );
      }
    } );
  },

  after: function() {
    return domManip( this, arguments, function( elem ) {
      if ( this.parentNode ) {
        this.parentNode.insertBefore( elem, this.nextSibling );
      }
    } );
  },

  empty: function() {
    var elem,
      i = 0;

    for ( ; ( elem = this[ i ] ) != null; i++ ) {
      if ( elem.nodeType === 1 ) {

        // Prevent memory leaks
        jQuery.cleanData( getAll( elem, false ) );

        // Remove any remaining nodes
        elem.textContent = "";
      }
    }

    return this;
  },

  clone: function( dataAndEvents, deepDataAndEvents ) {
    dataAndEvents = dataAndEvents == null ? false : dataAndEvents;
    deepDataAndEvents = deepDataAndEvents == null ? dataAndEvents : deepDataAndEvents;

    return this.map( function() {
      return jQuery.clone( this, dataAndEvents, deepDataAndEvents );
    } );
  },

  html: function( value ) {
    return access( this, function( value ) {
      var elem = this[ 0 ] || {},
        i = 0,
        l = this.length;

      if ( value === undefined && elem.nodeType === 1 ) {
        return elem.innerHTML;
      }

      // See if we can take a shortcut and just use innerHTML
      if ( typeof value === "string" && !rnoInnerhtml.test( value ) &&
        !wrapMap[ ( rtagName.exec( value ) || [ "", "" ] )[ 1 ].toLowerCase() ] ) {

        value = jQuery.htmlPrefilter( value );

        try {
          for ( ; i < l; i++ ) {
            elem = this[ i ] || {};

            // Remove element nodes and prevent memory leaks
            if ( elem.nodeType === 1 ) {
              jQuery.cleanData( getAll( elem, false ) );
              elem.innerHTML = value;
            }
          }

          elem = 0;

        // If using innerHTML throws an exception, use the fallback method
        } catch ( e ) {}
      }

      if ( elem ) {
        this.empty().append( value );
      }
    }, null, value, arguments.length );
  },

  replaceWith: function() {
    var ignored = [];

    // Make the changes, replacing each non-ignored context element with the new content
    return domManip( this, arguments, function( elem ) {
      var parent = this.parentNode;

      if ( jQuery.inArray( this, ignored ) < 0 ) {
        jQuery.cleanData( getAll( this ) );
        if ( parent ) {
          parent.replaceChild( elem, this );
        }
      }

    // Force callback invocation
    }, ignored );
  }
} );

jQuery.each( {
  appendTo: "append",
  prependTo: "prepend",
  insertBefore: "before",
  insertAfter: "after",
  replaceAll: "replaceWith"
}, function( name, original ) {
  jQuery.fn[ name ] = function( selector ) {
    var elems,
      ret = [],
      insert = jQuery( selector ),
      last = insert.length - 1,
      i = 0;

    for ( ; i <= last; i++ ) {
      elems = i === last ? this : this.clone( true );
      jQuery( insert[ i ] )[ original ]( elems );

      // Support: QtWebKit
      // .get() because push.apply(_, arraylike) throws
      push.apply( ret, elems.get() );
    }

    return this.pushStack( ret );
  };
} );


var iframe,
  elemdisplay = {

    // Support: Firefox
    // We have to pre-define these values for FF (#10227)
    HTML: "block",
    BODY: "block"
  };

/**
 * Retrieve the actual display of a element
 * @param {String} name nodeName of the element
 * @param {Object} doc Document object
 */

// Called only from within defaultDisplay
function actualDisplay( name, doc ) {
  var elem = jQuery( doc.createElement( name ) ).appendTo( doc.body ),

    display = jQuery.css( elem[ 0 ], "display" );

  // We don't have any data stored on the element,
  // so use "detach" method as fast way to get rid of the element
  elem.detach();

  return display;
}

/**
 * Try to determine the default display value of an element
 * @param {String} nodeName
 */
function defaultDisplay( nodeName ) {
  var doc = document,
    display = elemdisplay[ nodeName ];

  if ( !display ) {
    display = actualDisplay( nodeName, doc );

    // If the simple way fails, read from inside an iframe
    if ( display === "none" || !display ) {

      // Use the already-created iframe if possible
      iframe = ( iframe || jQuery( "<iframe frameborder='0' width='0' height='0'/>" ) )
        .appendTo( doc.documentElement );

      // Always write a new HTML skeleton so Webkit and Firefox don't choke on reuse
      doc = iframe[ 0 ].contentDocument;

      // Support: IE
      doc.write();
      doc.close();

      display = actualDisplay( nodeName, doc );
      iframe.detach();
    }

    // Store the correct default display
    elemdisplay[ nodeName ] = display;
  }

  return display;
}
var rmargin = ( /^margin/ );

var rnumnonpx = new RegExp( "^(" + pnum + ")(?!px)[a-z%]+$", "i" );

var getStyles = function( elem ) {

    // Support: IE<=11+, Firefox<=30+ (#15098, #14150)
    // IE throws on elements created in popups
    // FF meanwhile throws on frame elements through "defaultView.getComputedStyle"
    var view = elem.ownerDocument.defaultView;

    if ( !view || !view.opener ) {
      view = window;
    }

    return view.getComputedStyle( elem );
  };

var swap = function( elem, options, callback, args ) {
  var ret, name,
    old = {};

  // Remember the old values, and insert the new ones
  for ( name in options ) {
    old[ name ] = elem.style[ name ];
    elem.style[ name ] = options[ name ];
  }

  ret = callback.apply( elem, args || [] );

  // Revert the old values
  for ( name in options ) {
    elem.style[ name ] = old[ name ];
  }

  return ret;
};


var documentElement = document.documentElement;



( function() {
  var pixelPositionVal, boxSizingReliableVal, pixelMarginRightVal, reliableMarginLeftVal,
    container = document.createElement( "div" ),
    div = document.createElement( "div" );

  // Finish early in limited (non-browser) environments
  if ( !div.style ) {
    return;
  }

  // Support: IE9-11+
  // Style of cloned element affects source element cloned (#8908)
  div.style.backgroundClip = "content-box";
  div.cloneNode( true ).style.backgroundClip = "";
  support.clearCloneStyle = div.style.backgroundClip === "content-box";

  container.style.cssText = "border:0;width:8px;height:0;top:0;left:-9999px;" +
    "padding:0;margin-top:1px;position:absolute";
  container.appendChild( div );

  // Executing both pixelPosition & boxSizingReliable tests require only one layout
  // so they're executed at the same time to save the second computation.
  function computeStyleTests() {
    div.style.cssText =

      // Support: Firefox<29, Android 2.3
      // Vendor-prefix box-sizing
      "-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;" +
      "position:relative;display:block;" +
      "margin:auto;border:1px;padding:1px;" +
      "top:1%;width:50%";
    div.innerHTML = "";
    documentElement.appendChild( container );

    var divStyle = window.getComputedStyle( div );
    pixelPositionVal = divStyle.top !== "1%";
    reliableMarginLeftVal = divStyle.marginLeft === "2px";
    boxSizingReliableVal = divStyle.width === "4px";

    // Support: Android 4.0 - 4.3 only
    // Some styles come back with percentage values, even though they shouldn't
    div.style.marginRight = "50%";
    pixelMarginRightVal = divStyle.marginRight === "4px";

    documentElement.removeChild( container );
  }

  jQuery.extend( support, {
    pixelPosition: function() {

      // This test is executed only once but we still do memoizing
      // since we can use the boxSizingReliable pre-computing.
      // No need to check if the test was already performed, though.
      computeStyleTests();
      return pixelPositionVal;
    },
    boxSizingReliable: function() {
      if ( boxSizingReliableVal == null ) {
        computeStyleTests();
      }
      return boxSizingReliableVal;
    },
    pixelMarginRight: function() {

      // Support: Android 4.0-4.3
      // We're checking for boxSizingReliableVal here instead of pixelMarginRightVal
      // since that compresses better and they're computed together anyway.
      if ( boxSizingReliableVal == null ) {
        computeStyleTests();
      }
      return pixelMarginRightVal;
    },
    reliableMarginLeft: function() {

      // Support: IE <=8 only, Android 4.0 - 4.3 only, Firefox <=3 - 37
      if ( boxSizingReliableVal == null ) {
        computeStyleTests();
      }
      return reliableMarginLeftVal;
    },
    reliableMarginRight: function() {

      // Support: Android 2.3
      // Check if div with explicit width and no margin-right incorrectly
      // gets computed margin-right based on width of container. (#3333)
      // WebKit Bug 13343 - getComputedStyle returns wrong value for margin-right
      // This support function is only executed once so no memoizing is needed.
      var ret,
        marginDiv = div.appendChild( document.createElement( "div" ) );

      // Reset CSS: box-sizing; display; margin; border; padding
      marginDiv.style.cssText = div.style.cssText =

        // Support: Android 2.3
        // Vendor-prefix box-sizing
        "-webkit-box-sizing:content-box;box-sizing:content-box;" +
        "display:block;margin:0;border:0;padding:0";
      marginDiv.style.marginRight = marginDiv.style.width = "0";
      div.style.width = "1px";
      documentElement.appendChild( container );

      ret = !parseFloat( window.getComputedStyle( marginDiv ).marginRight );

      documentElement.removeChild( container );
      div.removeChild( marginDiv );

      return ret;
    }
  } );
} )();


function curCSS( elem, name, computed ) {
  var width, minWidth, maxWidth, ret,
    style = elem.style;

  computed = computed || getStyles( elem );
  ret = computed ? computed.getPropertyValue( name ) || computed[ name ] : undefined;

  // Support: Opera 12.1x only
  // Fall back to style even without computed
  // computed is undefined for elems on document fragments
  if ( ( ret === "" || ret === undefined ) && !jQuery.contains( elem.ownerDocument, elem ) ) {
    ret = jQuery.style( elem, name );
  }

  // Support: IE9
  // getPropertyValue is only needed for .css('filter') (#12537)
  if ( computed ) {

    // A tribute to the "awesome hack by Dean Edwards"
    // Android Browser returns percentage for some values,
    // but width seems to be reliably pixels.
    // This is against the CSSOM draft spec:
    // http://dev.w3.org/csswg/cssom/#resolved-values
    if ( !support.pixelMarginRight() && rnumnonpx.test( ret ) && rmargin.test( name ) ) {

      // Remember the original values
      width = style.width;
      minWidth = style.minWidth;
      maxWidth = style.maxWidth;

      // Put in the new values to get a computed value out
      style.minWidth = style.maxWidth = style.width = ret;
      ret = computed.width;

      // Revert the changed values
      style.width = width;
      style.minWidth = minWidth;
      style.maxWidth = maxWidth;
    }
  }

  return ret !== undefined ?

    // Support: IE9-11+
    // IE returns zIndex value as an integer.
    ret + "" :
    ret;
}


function addGetHookIf( conditionFn, hookFn ) {

  // Define the hook, we'll check on the first run if it's really needed.
  return {
    get: function() {
      if ( conditionFn() ) {

        // Hook not needed (or it's not possible to use it due
        // to missing dependency), remove it.
        delete this.get;
        return;
      }

      // Hook needed; redefine it so that the support test is not executed again.
      return ( this.get = hookFn ).apply( this, arguments );
    }
  };
}


var

  // Swappable if display is none or starts with table
  // except "table", "table-cell", or "table-caption"
  // See here for display values: https://developer.mozilla.org/en-US/docs/CSS/display
  rdisplayswap = /^(none|table(?!-c[ea]).+)/,

  cssShow = { position: "absolute", visibility: "hidden", display: "block" },
  cssNormalTransform = {
    letterSpacing: "0",
    fontWeight: "400"
  },

  cssPrefixes = [ "Webkit", "O", "Moz", "ms" ],
  emptyStyle = document.createElement( "div" ).style;

// Return a css property mapped to a potentially vendor prefixed property
function vendorPropName( name ) {

  // Shortcut for names that are not vendor prefixed
  if ( name in emptyStyle ) {
    return name;
  }

  // Check for vendor prefixed names
  var capName = name[ 0 ].toUpperCase() + name.slice( 1 ),
    i = cssPrefixes.length;

  while ( i-- ) {
    name = cssPrefixes[ i ] + capName;
    if ( name in emptyStyle ) {
      return name;
    }
  }
}

function setPositiveNumber( elem, value, subtract ) {

  // Any relative (+/-) values have already been
  // normalized at this point
  var matches = rcssNum.exec( value );
  return matches ?

    // Guard against undefined "subtract", e.g., when used as in cssHooks
    Math.max( 0, matches[ 2 ] - ( subtract || 0 ) ) + ( matches[ 3 ] || "px" ) :
    value;
}

function augmentWidthOrHeight( elem, name, extra, isBorderBox, styles ) {
  var i = extra === ( isBorderBox ? "border" : "content" ) ?

    // If we already have the right measurement, avoid augmentation
    4 :

    // Otherwise initialize for horizontal or vertical properties
    name === "width" ? 1 : 0,

    val = 0;

  for ( ; i < 4; i += 2 ) {

    // Both box models exclude margin, so add it if we want it
    if ( extra === "margin" ) {
      val += jQuery.css( elem, extra + cssExpand[ i ], true, styles );
    }

    if ( isBorderBox ) {

      // border-box includes padding, so remove it if we want content
      if ( extra === "content" ) {
        val -= jQuery.css( elem, "padding" + cssExpand[ i ], true, styles );
      }

      // At this point, extra isn't border nor margin, so remove border
      if ( extra !== "margin" ) {
        val -= jQuery.css( elem, "border" + cssExpand[ i ] + "Width", true, styles );
      }
    } else {

      // At this point, extra isn't content, so add padding
      val += jQuery.css( elem, "padding" + cssExpand[ i ], true, styles );

      // At this point, extra isn't content nor padding, so add border
      if ( extra !== "padding" ) {
        val += jQuery.css( elem, "border" + cssExpand[ i ] + "Width", true, styles );
      }
    }
  }

  return val;
}

function getWidthOrHeight( elem, name, extra ) {

  // Start with offset property, which is equivalent to the border-box value
  var valueIsBorderBox = true,
    val = name === "width" ? elem.offsetWidth : elem.offsetHeight,
    styles = getStyles( elem ),
    isBorderBox = jQuery.css( elem, "boxSizing", false, styles ) === "border-box";

  // Some non-html elements return undefined for offsetWidth, so check for null/undefined
  // svg - https://bugzilla.mozilla.org/show_bug.cgi?id=649285
  // MathML - https://bugzilla.mozilla.org/show_bug.cgi?id=491668
  if ( val <= 0 || val == null ) {

    // Fall back to computed then uncomputed css if necessary
    val = curCSS( elem, name, styles );
    if ( val < 0 || val == null ) {
      val = elem.style[ name ];
    }

    // Computed unit is not pixels. Stop here and return.
    if ( rnumnonpx.test( val ) ) {
      return val;
    }

    // Check for style in case a browser which returns unreliable values
    // for getComputedStyle silently falls back to the reliable elem.style
    valueIsBorderBox = isBorderBox &&
      ( support.boxSizingReliable() || val === elem.style[ name ] );

    // Normalize "", auto, and prepare for extra
    val = parseFloat( val ) || 0;
  }

  // Use the active box-sizing model to add/subtract irrelevant styles
  return ( val +
    augmentWidthOrHeight(
      elem,
      name,
      extra || ( isBorderBox ? "border" : "content" ),
      valueIsBorderBox,
      styles
    )
  ) + "px";
}

function showHide( elements, show ) {
  var display, elem, hidden,
    values = [],
    index = 0,
    length = elements.length;

  for ( ; index < length; index++ ) {
    elem = elements[ index ];
    if ( !elem.style ) {
      continue;
    }

    values[ index ] = dataPriv.get( elem, "olddisplay" );
    display = elem.style.display;
    if ( show ) {

      // Reset the inline display of this element to learn if it is
      // being hidden by cascaded rules or not
      if ( !values[ index ] && display === "none" ) {
        elem.style.display = "";
      }

      // Set elements which have been overridden with display: none
      // in a stylesheet to whatever the default browser style is
      // for such an element
      if ( elem.style.display === "" && isHidden( elem ) ) {
        values[ index ] = dataPriv.access(
          elem,
          "olddisplay",
          defaultDisplay( elem.nodeName )
        );
      }
    } else {
      hidden = isHidden( elem );

      if ( display !== "none" || !hidden ) {
        dataPriv.set(
          elem,
          "olddisplay",
          hidden ? display : jQuery.css( elem, "display" )
        );
      }
    }
  }

  // Set the display of most of the elements in a second loop
  // to avoid the constant reflow
  for ( index = 0; index < length; index++ ) {
    elem = elements[ index ];
    if ( !elem.style ) {
      continue;
    }
    if ( !show || elem.style.display === "none" || elem.style.display === "" ) {
      elem.style.display = show ? values[ index ] || "" : "none";
    }
  }

  return elements;
}

jQuery.extend( {

  // Add in style property hooks for overriding the default
  // behavior of getting and setting a style property
  cssHooks: {
    opacity: {
      get: function( elem, computed ) {
        if ( computed ) {

          // We should always get a number back from opacity
          var ret = curCSS( elem, "opacity" );
          return ret === "" ? "1" : ret;
        }
      }
    }
  },

  // Don't automatically add "px" to these possibly-unitless properties
  cssNumber: {
    "animationIterationCount": true,
    "columnCount": true,
    "fillOpacity": true,
    "flexGrow": true,
    "flexShrink": true,
    "fontWeight": true,
    "lineHeight": true,
    "opacity": true,
    "order": true,
    "orphans": true,
    "widows": true,
    "zIndex": true,
    "zoom": true
  },

  // Add in properties whose names you wish to fix before
  // setting or getting the value
  cssProps: {
    "float": "cssFloat"
  },

  // Get and set the style property on a DOM Node
  style: function( elem, name, value, extra ) {

    // Don't set styles on text and comment nodes
    if ( !elem || elem.nodeType === 3 || elem.nodeType === 8 || !elem.style ) {
      return;
    }

    // Make sure that we're working with the right name
    var ret, type, hooks,
      origName = jQuery.camelCase( name ),
      style = elem.style;

    name = jQuery.cssProps[ origName ] ||
      ( jQuery.cssProps[ origName ] = vendorPropName( origName ) || origName );

    // Gets hook for the prefixed version, then unprefixed version
    hooks = jQuery.cssHooks[ name ] || jQuery.cssHooks[ origName ];

    // Check if we're setting a value
    if ( value !== undefined ) {
      type = typeof value;

      // Convert "+=" or "-=" to relative numbers (#7345)
      if ( type === "string" && ( ret = rcssNum.exec( value ) ) && ret[ 1 ] ) {
        value = adjustCSS( elem, name, ret );

        // Fixes bug #9237
        type = "number";
      }

      // Make sure that null and NaN values aren't set (#7116)
      if ( value == null || value !== value ) {
        return;
      }

      // If a number was passed in, add the unit (except for certain CSS properties)
      if ( type === "number" ) {
        value += ret && ret[ 3 ] || ( jQuery.cssNumber[ origName ] ? "" : "px" );
      }

      // Support: IE9-11+
      // background-* props affect original clone's values
      if ( !support.clearCloneStyle && value === "" && name.indexOf( "background" ) === 0 ) {
        style[ name ] = "inherit";
      }

      // If a hook was provided, use that value, otherwise just set the specified value
      if ( !hooks || !( "set" in hooks ) ||
        ( value = hooks.set( elem, value, extra ) ) !== undefined ) {

        style[ name ] = value;
      }

    } else {

      // If a hook was provided get the non-computed value from there
      if ( hooks && "get" in hooks &&
        ( ret = hooks.get( elem, false, extra ) ) !== undefined ) {

        return ret;
      }

      // Otherwise just get the value from the style object
      return style[ name ];
    }
  },

  css: function( elem, name, extra, styles ) {
    var val, num, hooks,
      origName = jQuery.camelCase( name );

    // Make sure that we're working with the right name
    name = jQuery.cssProps[ origName ] ||
      ( jQuery.cssProps[ origName ] = vendorPropName( origName ) || origName );

    // Try prefixed name followed by the unprefixed name
    hooks = jQuery.cssHooks[ name ] || jQuery.cssHooks[ origName ];

    // If a hook was provided get the computed value from there
    if ( hooks && "get" in hooks ) {
      val = hooks.get( elem, true, extra );
    }

    // Otherwise, if a way to get the computed value exists, use that
    if ( val === undefined ) {
      val = curCSS( elem, name, styles );
    }

    // Convert "normal" to computed value
    if ( val === "normal" && name in cssNormalTransform ) {
      val = cssNormalTransform[ name ];
    }

    // Make numeric if forced or a qualifier was provided and val looks numeric
    if ( extra === "" || extra ) {
      num = parseFloat( val );
      return extra === true || isFinite( num ) ? num || 0 : val;
    }
    return val;
  }
} );

jQuery.each( [ "height", "width" ], function( i, name ) {
  jQuery.cssHooks[ name ] = {
    get: function( elem, computed, extra ) {
      if ( computed ) {

        // Certain elements can have dimension info if we invisibly show them
        // but it must have a current display style that would benefit
        return rdisplayswap.test( jQuery.css( elem, "display" ) ) &&
          elem.offsetWidth === 0 ?
            swap( elem, cssShow, function() {
              return getWidthOrHeight( elem, name, extra );
            } ) :
            getWidthOrHeight( elem, name, extra );
      }
    },

    set: function( elem, value, extra ) {
      var matches,
        styles = extra && getStyles( elem ),
        subtract = extra && augmentWidthOrHeight(
          elem,
          name,
          extra,
          jQuery.css( elem, "boxSizing", false, styles ) === "border-box",
          styles
        );

      // Convert to pixels if value adjustment is needed
      if ( subtract && ( matches = rcssNum.exec( value ) ) &&
        ( matches[ 3 ] || "px" ) !== "px" ) {

        elem.style[ name ] = value;
        value = jQuery.css( elem, name );
      }

      return setPositiveNumber( elem, value, subtract );
    }
  };
} );

jQuery.cssHooks.marginLeft = addGetHookIf( support.reliableMarginLeft,
  function( elem, computed ) {
    if ( computed ) {
      return ( parseFloat( curCSS( elem, "marginLeft" ) ) ||
        elem.getBoundingClientRect().left -
          swap( elem, { marginLeft: 0 }, function() {
            return elem.getBoundingClientRect().left;
          } )
        ) + "px";
    }
  }
);

// Support: Android 2.3
jQuery.cssHooks.marginRight = addGetHookIf( support.reliableMarginRight,
  function( elem, computed ) {
    if ( computed ) {
      return swap( elem, { "display": "inline-block" },
        curCSS, [ elem, "marginRight" ] );
    }
  }
);

// These hooks are used by animate to expand properties
jQuery.each( {
  margin: "",
  padding: "",
  border: "Width"
}, function( prefix, suffix ) {
  jQuery.cssHooks[ prefix + suffix ] = {
    expand: function( value ) {
      var i = 0,
        expanded = {},

        // Assumes a single number if not a string
        parts = typeof value === "string" ? value.split( " " ) : [ value ];

      for ( ; i < 4; i++ ) {
        expanded[ prefix + cssExpand[ i ] + suffix ] =
          parts[ i ] || parts[ i - 2 ] || parts[ 0 ];
      }

      return expanded;
    }
  };

  if ( !rmargin.test( prefix ) ) {
    jQuery.cssHooks[ prefix + suffix ].set = setPositiveNumber;
  }
} );

jQuery.fn.extend( {
  css: function( name, value ) {
    return access( this, function( elem, name, value ) {
      var styles, len,
        map = {},
        i = 0;

      if ( jQuery.isArray( name ) ) {
        styles = getStyles( elem );
        len = name.length;

        for ( ; i < len; i++ ) {
          map[ name[ i ] ] = jQuery.css( elem, name[ i ], false, styles );
        }

        return map;
      }

      return value !== undefined ?
        jQuery.style( elem, name, value ) :
        jQuery.css( elem, name );
    }, name, value, arguments.length > 1 );
  },
  show: function() {
    return showHide( this, true );
  },
  hide: function() {
    return showHide( this );
  },
  toggle: function( state ) {
    if ( typeof state === "boolean" ) {
      return state ? this.show() : this.hide();
    }

    return this.each( function() {
      if ( isHidden( this ) ) {
        jQuery( this ).show();
      } else {
        jQuery( this ).hide();
      }
    } );
  }
} );


function Tween( elem, options, prop, end, easing ) {
  return new Tween.prototype.init( elem, options, prop, end, easing );
}
jQuery.Tween = Tween;

Tween.prototype = {
  constructor: Tween,
  init: function( elem, options, prop, end, easing, unit ) {
    this.elem = elem;
    this.prop = prop;
    this.easing = easing || jQuery.easing._default;
    this.options = options;
    this.start = this.now = this.cur();
    this.end = end;
    this.unit = unit || ( jQuery.cssNumber[ prop ] ? "" : "px" );
  },
  cur: function() {
    var hooks = Tween.propHooks[ this.prop ];

    return hooks && hooks.get ?
      hooks.get( this ) :
      Tween.propHooks._default.get( this );
  },
  run: function( percent ) {
    var eased,
      hooks = Tween.propHooks[ this.prop ];

    if ( this.options.duration ) {
      this.pos = eased = jQuery.easing[ this.easing ](
        percent, this.options.duration * percent, 0, 1, this.options.duration
      );
    } else {
      this.pos = eased = percent;
    }
    this.now = ( this.end - this.start ) * eased + this.start;

    if ( this.options.step ) {
      this.options.step.call( this.elem, this.now, this );
    }

    if ( hooks && hooks.set ) {
      hooks.set( this );
    } else {
      Tween.propHooks._default.set( this );
    }
    return this;
  }
};

Tween.prototype.init.prototype = Tween.prototype;

Tween.propHooks = {
  _default: {
    get: function( tween ) {
      var result;

      // Use a property on the element directly when it is not a DOM element,
      // or when there is no matching style property that exists.
      if ( tween.elem.nodeType !== 1 ||
        tween.elem[ tween.prop ] != null && tween.elem.style[ tween.prop ] == null ) {
        return tween.elem[ tween.prop ];
      }

      // Passing an empty string as a 3rd parameter to .css will automatically
      // attempt a parseFloat and fallback to a string if the parse fails.
      // Simple values such as "10px" are parsed to Float;
      // complex values such as "rotate(1rad)" are returned as-is.
      result = jQuery.css( tween.elem, tween.prop, "" );

      // Empty strings, null, undefined and "auto" are converted to 0.
      return !result || result === "auto" ? 0 : result;
    },
    set: function( tween ) {

      // Use step hook for back compat.
      // Use cssHook if its there.
      // Use .style if available and use plain properties where available.
      if ( jQuery.fx.step[ tween.prop ] ) {
        jQuery.fx.step[ tween.prop ]( tween );
      } else if ( tween.elem.nodeType === 1 &&
        ( tween.elem.style[ jQuery.cssProps[ tween.prop ] ] != null ||
          jQuery.cssHooks[ tween.prop ] ) ) {
        jQuery.style( tween.elem, tween.prop, tween.now + tween.unit );
      } else {
        tween.elem[ tween.prop ] = tween.now;
      }
    }
  }
};

// Support: IE9
// Panic based approach to setting things on disconnected nodes
Tween.propHooks.scrollTop = Tween.propHooks.scrollLeft = {
  set: function( tween ) {
    if ( tween.elem.nodeType && tween.elem.parentNode ) {
      tween.elem[ tween.prop ] = tween.now;
    }
  }
};

jQuery.easing = {
  linear: function( p ) {
    return p;
  },
  swing: function( p ) {
    return 0.5 - Math.cos( p * Math.PI ) / 2;
  },
  _default: "swing"
};

jQuery.fx = Tween.prototype.init;

// Back Compat <1.8 extension point
jQuery.fx.step = {};




var
  fxNow, timerId,
  rfxtypes = /^(?:toggle|show|hide)$/,
  rrun = /queueHooks$/;

// Animations created synchronously will run synchronously
function createFxNow() {
  window.setTimeout( function() {
    fxNow = undefined;
  } );
  return ( fxNow = jQuery.now() );
}

// Generate parameters to create a standard animation
function genFx( type, includeWidth ) {
  var which,
    i = 0,
    attrs = { height: type };

  // If we include width, step value is 1 to do all cssExpand values,
  // otherwise step value is 2 to skip over Left and Right
  includeWidth = includeWidth ? 1 : 0;
  for ( ; i < 4 ; i += 2 - includeWidth ) {
    which = cssExpand[ i ];
    attrs[ "margin" + which ] = attrs[ "padding" + which ] = type;
  }

  if ( includeWidth ) {
    attrs.opacity = attrs.width = type;
  }

  return attrs;
}

function createTween( value, prop, animation ) {
  var tween,
    collection = ( Animation.tweeners[ prop ] || [] ).concat( Animation.tweeners[ "*" ] ),
    index = 0,
    length = collection.length;
  for ( ; index < length; index++ ) {
    if ( ( tween = collection[ index ].call( animation, prop, value ) ) ) {

      // We're done with this property
      return tween;
    }
  }
}

function defaultPrefilter( elem, props, opts ) {
  /* jshint validthis: true */
  var prop, value, toggle, tween, hooks, oldfire, display, checkDisplay,
    anim = this,
    orig = {},
    style = elem.style,
    hidden = elem.nodeType && isHidden( elem ),
    dataShow = dataPriv.get( elem, "fxshow" );

  // Handle queue: false promises
  if ( !opts.queue ) {
    hooks = jQuery._queueHooks( elem, "fx" );
    if ( hooks.unqueued == null ) {
      hooks.unqueued = 0;
      oldfire = hooks.empty.fire;
      hooks.empty.fire = function() {
        if ( !hooks.unqueued ) {
          oldfire();
        }
      };
    }
    hooks.unqueued++;

    anim.always( function() {

      // Ensure the complete handler is called before this completes
      anim.always( function() {
        hooks.unqueued--;
        if ( !jQuery.queue( elem, "fx" ).length ) {
          hooks.empty.fire();
        }
      } );
    } );
  }

  // Height/width overflow pass
  if ( elem.nodeType === 1 && ( "height" in props || "width" in props ) ) {

    // Make sure that nothing sneaks out
    // Record all 3 overflow attributes because IE9-10 do not
    // change the overflow attribute when overflowX and
    // overflowY are set to the same value
    opts.overflow = [ style.overflow, style.overflowX, style.overflowY ];

    // Set display property to inline-block for height/width
    // animations on inline elements that are having width/height animated
    display = jQuery.css( elem, "display" );

    // Test default display if display is currently "none"
    checkDisplay = display === "none" ?
      dataPriv.get( elem, "olddisplay" ) || defaultDisplay( elem.nodeName ) : display;

    if ( checkDisplay === "inline" && jQuery.css( elem, "float" ) === "none" ) {
      style.display = "inline-block";
    }
  }

  if ( opts.overflow ) {
    style.overflow = "hidden";
    anim.always( function() {
      style.overflow = opts.overflow[ 0 ];
      style.overflowX = opts.overflow[ 1 ];
      style.overflowY = opts.overflow[ 2 ];
    } );
  }

  // show/hide pass
  for ( prop in props ) {
    value = props[ prop ];
    if ( rfxtypes.exec( value ) ) {
      delete props[ prop ];
      toggle = toggle || value === "toggle";
      if ( value === ( hidden ? "hide" : "show" ) ) {

        // If there is dataShow left over from a stopped hide or show
        // and we are going to proceed with show, we should pretend to be hidden
        if ( value === "show" && dataShow && dataShow[ prop ] !== undefined ) {
          hidden = true;
        } else {
          continue;
        }
      }
      orig[ prop ] = dataShow && dataShow[ prop ] || jQuery.style( elem, prop );

    // Any non-fx value stops us from restoring the original display value
    } else {
      display = undefined;
    }
  }

  if ( !jQuery.isEmptyObject( orig ) ) {
    if ( dataShow ) {
      if ( "hidden" in dataShow ) {
        hidden = dataShow.hidden;
      }
    } else {
      dataShow = dataPriv.access( elem, "fxshow", {} );
    }

    // Store state if its toggle - enables .stop().toggle() to "reverse"
    if ( toggle ) {
      dataShow.hidden = !hidden;
    }
    if ( hidden ) {
      jQuery( elem ).show();
    } else {
      anim.done( function() {
        jQuery( elem ).hide();
      } );
    }
    anim.done( function() {
      var prop;

      dataPriv.remove( elem, "fxshow" );
      for ( prop in orig ) {
        jQuery.style( elem, prop, orig[ prop ] );
      }
    } );
    for ( prop in orig ) {
      tween = createTween( hidden ? dataShow[ prop ] : 0, prop, anim );

      if ( !( prop in dataShow ) ) {
        dataShow[ prop ] = tween.start;
        if ( hidden ) {
          tween.end = tween.start;
          tween.start = prop === "width" || prop === "height" ? 1 : 0;
        }
      }
    }

  // If this is a noop like .hide().hide(), restore an overwritten display value
  } else if ( ( display === "none" ? defaultDisplay( elem.nodeName ) : display ) === "inline" ) {
    style.display = display;
  }
}

function propFilter( props, specialEasing ) {
  var index, name, easing, value, hooks;

  // camelCase, specialEasing and expand cssHook pass
  for ( index in props ) {
    name = jQuery.camelCase( index );
    easing = specialEasing[ name ];
    value = props[ index ];
    if ( jQuery.isArray( value ) ) {
      easing = value[ 1 ];
      value = props[ index ] = value[ 0 ];
    }

    if ( index !== name ) {
      props[ name ] = value;
      delete props[ index ];
    }

    hooks = jQuery.cssHooks[ name ];
    if ( hooks && "expand" in hooks ) {
      value = hooks.expand( value );
      delete props[ name ];

      // Not quite $.extend, this won't overwrite existing keys.
      // Reusing 'index' because we have the correct "name"
      for ( index in value ) {
        if ( !( index in props ) ) {
          props[ index ] = value[ index ];
          specialEasing[ index ] = easing;
        }
      }
    } else {
      specialEasing[ name ] = easing;
    }
  }
}

function Animation( elem, properties, options ) {
  var result,
    stopped,
    index = 0,
    length = Animation.prefilters.length,
    deferred = jQuery.Deferred().always( function() {

      // Don't match elem in the :animated selector
      delete tick.elem;
    } ),
    tick = function() {
      if ( stopped ) {
        return false;
      }
      var currentTime = fxNow || createFxNow(),
        remaining = Math.max( 0, animation.startTime + animation.duration - currentTime ),

        // Support: Android 2.3
        // Archaic crash bug won't allow us to use `1 - ( 0.5 || 0 )` (#12497)
        temp = remaining / animation.duration || 0,
        percent = 1 - temp,
        index = 0,
        length = animation.tweens.length;

      for ( ; index < length ; index++ ) {
        animation.tweens[ index ].run( percent );
      }

      deferred.notifyWith( elem, [ animation, percent, remaining ] );

      if ( percent < 1 && length ) {
        return remaining;
      } else {
        deferred.resolveWith( elem, [ animation ] );
        return false;
      }
    },
    animation = deferred.promise( {
      elem: elem,
      props: jQuery.extend( {}, properties ),
      opts: jQuery.extend( true, {
        specialEasing: {},
        easing: jQuery.easing._default
      }, options ),
      originalProperties: properties,
      originalOptions: options,
      startTime: fxNow || createFxNow(),
      duration: options.duration,
      tweens: [],
      createTween: function( prop, end ) {
        var tween = jQuery.Tween( elem, animation.opts, prop, end,
            animation.opts.specialEasing[ prop ] || animation.opts.easing );
        animation.tweens.push( tween );
        return tween;
      },
      stop: function( gotoEnd ) {
        var index = 0,

          // If we are going to the end, we want to run all the tweens
          // otherwise we skip this part
          length = gotoEnd ? animation.tweens.length : 0;
        if ( stopped ) {
          return this;
        }
        stopped = true;
        for ( ; index < length ; index++ ) {
          animation.tweens[ index ].run( 1 );
        }

        // Resolve when we played the last frame; otherwise, reject
        if ( gotoEnd ) {
          deferred.notifyWith( elem, [ animation, 1, 0 ] );
          deferred.resolveWith( elem, [ animation, gotoEnd ] );
        } else {
          deferred.rejectWith( elem, [ animation, gotoEnd ] );
        }
        return this;
      }
    } ),
    props = animation.props;

  propFilter( props, animation.opts.specialEasing );

  for ( ; index < length ; index++ ) {
    result = Animation.prefilters[ index ].call( animation, elem, props, animation.opts );
    if ( result ) {
      if ( jQuery.isFunction( result.stop ) ) {
        jQuery._queueHooks( animation.elem, animation.opts.queue ).stop =
          jQuery.proxy( result.stop, result );
      }
      return result;
    }
  }

  jQuery.map( props, createTween, animation );

  if ( jQuery.isFunction( animation.opts.start ) ) {
    animation.opts.start.call( elem, animation );
  }

  jQuery.fx.timer(
    jQuery.extend( tick, {
      elem: elem,
      anim: animation,
      queue: animation.opts.queue
    } )
  );

  // attach callbacks from options
  return animation.progress( animation.opts.progress )
    .done( animation.opts.done, animation.opts.complete )
    .fail( animation.opts.fail )
    .always( animation.opts.always );
}

jQuery.Animation = jQuery.extend( Animation, {
  tweeners: {
    "*": [ function( prop, value ) {
      var tween = this.createTween( prop, value );
      adjustCSS( tween.elem, prop, rcssNum.exec( value ), tween );
      return tween;
    } ]
  },

  tweener: function( props, callback ) {
    if ( jQuery.isFunction( props ) ) {
      callback = props;
      props = [ "*" ];
    } else {
      props = props.match( rnotwhite );
    }

    var prop,
      index = 0,
      length = props.length;

    for ( ; index < length ; index++ ) {
      prop = props[ index ];
      Animation.tweeners[ prop ] = Animation.tweeners[ prop ] || [];
      Animation.tweeners[ prop ].unshift( callback );
    }
  },

  prefilters: [ defaultPrefilter ],

  prefilter: function( callback, prepend ) {
    if ( prepend ) {
      Animation.prefilters.unshift( callback );
    } else {
      Animation.prefilters.push( callback );
    }
  }
} );

jQuery.speed = function( speed, easing, fn ) {
  var opt = speed && typeof speed === "object" ? jQuery.extend( {}, speed ) : {
    complete: fn || !fn && easing ||
      jQuery.isFunction( speed ) && speed,
    duration: speed,
    easing: fn && easing || easing && !jQuery.isFunction( easing ) && easing
  };

  opt.duration = jQuery.fx.off ? 0 : typeof opt.duration === "number" ?
    opt.duration : opt.duration in jQuery.fx.speeds ?
      jQuery.fx.speeds[ opt.duration ] : jQuery.fx.speeds._default;

  // Normalize opt.queue - true/undefined/null -> "fx"
  if ( opt.queue == null || opt.queue === true ) {
    opt.queue = "fx";
  }

  // Queueing
  opt.old = opt.complete;

  opt.complete = function() {
    if ( jQuery.isFunction( opt.old ) ) {
      opt.old.call( this );
    }

    if ( opt.queue ) {
      jQuery.dequeue( this, opt.queue );
    }
  };

  return opt;
};

jQuery.fn.extend( {
  fadeTo: function( speed, to, easing, callback ) {

    // Show any hidden elements after setting opacity to 0
    return this.filter( isHidden ).css( "opacity", 0 ).show()

      // Animate to the value specified
      .end().animate( { opacity: to }, speed, easing, callback );
  },
  animate: function( prop, speed, easing, callback ) {
    var empty = jQuery.isEmptyObject( prop ),
      optall = jQuery.speed( speed, easing, callback ),
      doAnimation = function() {

        // Operate on a copy of prop so per-property easing won't be lost
        var anim = Animation( this, jQuery.extend( {}, prop ), optall );

        // Empty animations, or finishing resolves immediately
        if ( empty || dataPriv.get( this, "finish" ) ) {
          anim.stop( true );
        }
      };
      doAnimation.finish = doAnimation;

    return empty || optall.queue === false ?
      this.each( doAnimation ) :
      this.queue( optall.queue, doAnimation );
  },
  stop: function( type, clearQueue, gotoEnd ) {
    var stopQueue = function( hooks ) {
      var stop = hooks.stop;
      delete hooks.stop;
      stop( gotoEnd );
    };

    if ( typeof type !== "string" ) {
      gotoEnd = clearQueue;
      clearQueue = type;
      type = undefined;
    }
    if ( clearQueue && type !== false ) {
      this.queue( type || "fx", [] );
    }

    return this.each( function() {
      var dequeue = true,
        index = type != null && type + "queueHooks",
        timers = jQuery.timers,
        data = dataPriv.get( this );

      if ( index ) {
        if ( data[ index ] && data[ index ].stop ) {
          stopQueue( data[ index ] );
        }
      } else {
        for ( index in data ) {
          if ( data[ index ] && data[ index ].stop && rrun.test( index ) ) {
            stopQueue( data[ index ] );
          }
        }
      }

      for ( index = timers.length; index--; ) {
        if ( timers[ index ].elem === this &&
          ( type == null || timers[ index ].queue === type ) ) {

          timers[ index ].anim.stop( gotoEnd );
          dequeue = false;
          timers.splice( index, 1 );
        }
      }

      // Start the next in the queue if the last step wasn't forced.
      // Timers currently will call their complete callbacks, which
      // will dequeue but only if they were gotoEnd.
      if ( dequeue || !gotoEnd ) {
        jQuery.dequeue( this, type );
      }
    } );
  },
  finish: function( type ) {
    if ( type !== false ) {
      type = type || "fx";
    }
    return this.each( function() {
      var index,
        data = dataPriv.get( this ),
        queue = data[ type + "queue" ],
        hooks = data[ type + "queueHooks" ],
        timers = jQuery.timers,
        length = queue ? queue.length : 0;

      // Enable finishing flag on private data
      data.finish = true;

      // Empty the queue first
      jQuery.queue( this, type, [] );

      if ( hooks && hooks.stop ) {
        hooks.stop.call( this, true );
      }

      // Look for any active animations, and finish them
      for ( index = timers.length; index--; ) {
        if ( timers[ index ].elem === this && timers[ index ].queue === type ) {
          timers[ index ].anim.stop( true );
          timers.splice( index, 1 );
        }
      }

      // Look for any animations in the old queue and finish them
      for ( index = 0; index < length; index++ ) {
        if ( queue[ index ] && queue[ index ].finish ) {
          queue[ index ].finish.call( this );
        }
      }

      // Turn off finishing flag
      delete data.finish;
    } );
  }
} );

jQuery.each( [ "toggle", "show", "hide" ], function( i, name ) {
  var cssFn = jQuery.fn[ name ];
  jQuery.fn[ name ] = function( speed, easing, callback ) {
    return speed == null || typeof speed === "boolean" ?
      cssFn.apply( this, arguments ) :
      this.animate( genFx( name, true ), speed, easing, callback );
  };
} );

// Generate shortcuts for custom animations
jQuery.each( {
  slideDown: genFx( "show" ),
  slideUp: genFx( "hide" ),
  slideToggle: genFx( "toggle" ),
  fadeIn: { opacity: "show" },
  fadeOut: { opacity: "hide" },
  fadeToggle: { opacity: "toggle" }
}, function( name, props ) {
  jQuery.fn[ name ] = function( speed, easing, callback ) {
    return this.animate( props, speed, easing, callback );
  };
} );

jQuery.timers = [];
jQuery.fx.tick = function() {
  var timer,
    i = 0,
    timers = jQuery.timers;

  fxNow = jQuery.now();

  for ( ; i < timers.length; i++ ) {
    timer = timers[ i ];

    // Checks the timer has not already been removed
    if ( !timer() && timers[ i ] === timer ) {
      timers.splice( i--, 1 );
    }
  }

  if ( !timers.length ) {
    jQuery.fx.stop();
  }
  fxNow = undefined;
};

jQuery.fx.timer = function( timer ) {
  jQuery.timers.push( timer );
  if ( timer() ) {
    jQuery.fx.start();
  } else {
    jQuery.timers.pop();
  }
};

jQuery.fx.interval = 13;
jQuery.fx.start = function() {
  if ( !timerId ) {
    timerId = window.setInterval( jQuery.fx.tick, jQuery.fx.interval );
  }
};

jQuery.fx.stop = function() {
  window.clearInterval( timerId );

  timerId = null;
};

jQuery.fx.speeds = {
  slow: 600,
  fast: 200,

  // Default speed
  _default: 400
};


// Based off of the plugin by Clint Helfers, with permission.
// http://web.archive.org/web/20100324014747/http://blindsignals.com/index.php/2009/07/jquery-delay/
jQuery.fn.delay = function( time, type ) {
  time = jQuery.fx ? jQuery.fx.speeds[ time ] || time : time;
  type = type || "fx";

  return this.queue( type, function( next, hooks ) {
    var timeout = window.setTimeout( next, time );
    hooks.stop = function() {
      window.clearTimeout( timeout );
    };
  } );
};


( function() {
  var input = document.createElement( "input" ),
    select = document.createElement( "select" ),
    opt = select.appendChild( document.createElement( "option" ) );

  input.type = "checkbox";

  // Support: iOS<=5.1, Android<=4.2+
  // Default value for a checkbox should be "on"
  support.checkOn = input.value !== "";

  // Support: IE<=11+
  // Must access selectedIndex to make default options select
  support.optSelected = opt.selected;

  // Support: Android<=2.3
  // Options inside disabled selects are incorrectly marked as disabled
  select.disabled = true;
  support.optDisabled = !opt.disabled;

  // Support: IE<=11+
  // An input loses its value after becoming a radio
  input = document.createElement( "input" );
  input.value = "t";
  input.type = "radio";
  support.radioValue = input.value === "t";
} )();


var boolHook,
  attrHandle = jQuery.expr.attrHandle;

jQuery.fn.extend( {
  attr: function( name, value ) {
    return access( this, jQuery.attr, name, value, arguments.length > 1 );
  },

  removeAttr: function( name ) {
    return this.each( function() {
      jQuery.removeAttr( this, name );
    } );
  }
} );

jQuery.extend( {
  attr: function( elem, name, value ) {
    var ret, hooks,
      nType = elem.nodeType;

    // Don't get/set attributes on text, comment and attribute nodes
    if ( nType === 3 || nType === 8 || nType === 2 ) {
      return;
    }

    // Fallback to prop when attributes are not supported
    if ( typeof elem.getAttribute === "undefined" ) {
      return jQuery.prop( elem, name, value );
    }

    // All attributes are lowercase
    // Grab necessary hook if one is defined
    if ( nType !== 1 || !jQuery.isXMLDoc( elem ) ) {
      name = name.toLowerCase();
      hooks = jQuery.attrHooks[ name ] ||
        ( jQuery.expr.match.bool.test( name ) ? boolHook : undefined );
    }

    if ( value !== undefined ) {
      if ( value === null ) {
        jQuery.removeAttr( elem, name );
        return;
      }

      if ( hooks && "set" in hooks &&
        ( ret = hooks.set( elem, value, name ) ) !== undefined ) {
        return ret;
      }

      elem.setAttribute( name, value + "" );
      return value;
    }

    if ( hooks && "get" in hooks && ( ret = hooks.get( elem, name ) ) !== null ) {
      return ret;
    }

    ret = jQuery.find.attr( elem, name );

    // Non-existent attributes return null, we normalize to undefined
    return ret == null ? undefined : ret;
  },

  attrHooks: {
    type: {
      set: function( elem, value ) {
        if ( !support.radioValue && value === "radio" &&
          jQuery.nodeName( elem, "input" ) ) {
          var val = elem.value;
          elem.setAttribute( "type", value );
          if ( val ) {
            elem.value = val;
          }
          return value;
        }
      }
    }
  },

  removeAttr: function( elem, value ) {
    var name, propName,
      i = 0,
      attrNames = value && value.match( rnotwhite );

    if ( attrNames && elem.nodeType === 1 ) {
      while ( ( name = attrNames[ i++ ] ) ) {
        propName = jQuery.propFix[ name ] || name;

        // Boolean attributes get special treatment (#10870)
        if ( jQuery.expr.match.bool.test( name ) ) {

          // Set corresponding property to false
          elem[ propName ] = false;
        }

        elem.removeAttribute( name );
      }
    }
  }
} );

// Hooks for boolean attributes
boolHook = {
  set: function( elem, value, name ) {
    if ( value === false ) {

      // Remove boolean attributes when set to false
      jQuery.removeAttr( elem, name );
    } else {
      elem.setAttribute( name, name );
    }
    return name;
  }
};
jQuery.each( jQuery.expr.match.bool.source.match( /\w+/g ), function( i, name ) {
  var getter = attrHandle[ name ] || jQuery.find.attr;

  attrHandle[ name ] = function( elem, name, isXML ) {
    var ret, handle;
    if ( !isXML ) {

      // Avoid an infinite loop by temporarily removing this function from the getter
      handle = attrHandle[ name ];
      attrHandle[ name ] = ret;
      ret = getter( elem, name, isXML ) != null ?
        name.toLowerCase() :
        null;
      attrHandle[ name ] = handle;
    }
    return ret;
  };
} );




var rfocusable = /^(?:input|select|textarea|button)$/i,
  rclickable = /^(?:a|area)$/i;

jQuery.fn.extend( {
  prop: function( name, value ) {
    return access( this, jQuery.prop, name, value, arguments.length > 1 );
  },

  removeProp: function( name ) {
    return this.each( function() {
      delete this[ jQuery.propFix[ name ] || name ];
    } );
  }
} );

jQuery.extend( {
  prop: function( elem, name, value ) {
    var ret, hooks,
      nType = elem.nodeType;

    // Don't get/set properties on text, comment and attribute nodes
    if ( nType === 3 || nType === 8 || nType === 2 ) {
      return;
    }

    if ( nType !== 1 || !jQuery.isXMLDoc( elem ) ) {

      // Fix name and attach hooks
      name = jQuery.propFix[ name ] || name;
      hooks = jQuery.propHooks[ name ];
    }

    if ( value !== undefined ) {
      if ( hooks && "set" in hooks &&
        ( ret = hooks.set( elem, value, name ) ) !== undefined ) {
        return ret;
      }

      return ( elem[ name ] = value );
    }

    if ( hooks && "get" in hooks && ( ret = hooks.get( elem, name ) ) !== null ) {
      return ret;
    }

    return elem[ name ];
  },

  propHooks: {
    tabIndex: {
      get: function( elem ) {

        // elem.tabIndex doesn't always return the
        // correct value when it hasn't been explicitly set
        // http://fluidproject.org/blog/2008/01/09/getting-setting-and-removing-tabindex-values-with-javascript/
        // Use proper attribute retrieval(#12072)
        var tabindex = jQuery.find.attr( elem, "tabindex" );

        return tabindex ?
          parseInt( tabindex, 10 ) :
          rfocusable.test( elem.nodeName ) ||
            rclickable.test( elem.nodeName ) && elem.href ?
              0 :
              -1;
      }
    }
  },

  propFix: {
    "for": "htmlFor",
    "class": "className"
  }
} );

// Support: IE <=11 only
// Accessing the selectedIndex property
// forces the browser to respect setting selected
// on the option
// The getter ensures a default option is selected
// when in an optgroup
if ( !support.optSelected ) {
  jQuery.propHooks.selected = {
    get: function( elem ) {
      var parent = elem.parentNode;
      if ( parent && parent.parentNode ) {
        parent.parentNode.selectedIndex;
      }
      return null;
    },
    set: function( elem ) {
      var parent = elem.parentNode;
      if ( parent ) {
        parent.selectedIndex;

        if ( parent.parentNode ) {
          parent.parentNode.selectedIndex;
        }
      }
    }
  };
}

jQuery.each( [
  "tabIndex",
  "readOnly",
  "maxLength",
  "cellSpacing",
  "cellPadding",
  "rowSpan",
  "colSpan",
  "useMap",
  "frameBorder",
  "contentEditable"
], function() {
  jQuery.propFix[ this.toLowerCase() ] = this;
} );




var rclass = /[\t\r\n\f]/g;

function getClass( elem ) {
  return elem.getAttribute && elem.getAttribute( "class" ) || "";
}

jQuery.fn.extend( {
  addClass: function( value ) {
    var classes, elem, cur, curValue, clazz, j, finalValue,
      i = 0;

    if ( jQuery.isFunction( value ) ) {
      return this.each( function( j ) {
        jQuery( this ).addClass( value.call( this, j, getClass( this ) ) );
      } );
    }

    if ( typeof value === "string" && value ) {
      classes = value.match( rnotwhite ) || [];

      while ( ( elem = this[ i++ ] ) ) {
        curValue = getClass( elem );
        cur = elem.nodeType === 1 &&
          ( " " + curValue + " " ).replace( rclass, " " );

        if ( cur ) {
          j = 0;
          while ( ( clazz = classes[ j++ ] ) ) {
            if ( cur.indexOf( " " + clazz + " " ) < 0 ) {
              cur += clazz + " ";
            }
          }

          // Only assign if different to avoid unneeded rendering.
          finalValue = jQuery.trim( cur );
          if ( curValue !== finalValue ) {
            elem.setAttribute( "class", finalValue );
          }
        }
      }
    }

    return this;
  },

  removeClass: function( value ) {
    var classes, elem, cur, curValue, clazz, j, finalValue,
      i = 0;

    if ( jQuery.isFunction( value ) ) {
      return this.each( function( j ) {
        jQuery( this ).removeClass( value.call( this, j, getClass( this ) ) );
      } );
    }

    if ( !arguments.length ) {
      return this.attr( "class", "" );
    }

    if ( typeof value === "string" && value ) {
      classes = value.match( rnotwhite ) || [];

      while ( ( elem = this[ i++ ] ) ) {
        curValue = getClass( elem );

        // This expression is here for better compressibility (see addClass)
        cur = elem.nodeType === 1 &&
          ( " " + curValue + " " ).replace( rclass, " " );

        if ( cur ) {
          j = 0;
          while ( ( clazz = classes[ j++ ] ) ) {

            // Remove *all* instances
            while ( cur.indexOf( " " + clazz + " " ) > -1 ) {
              cur = cur.replace( " " + clazz + " ", " " );
            }
          }

          // Only assign if different to avoid unneeded rendering.
          finalValue = jQuery.trim( cur );
          if ( curValue !== finalValue ) {
            elem.setAttribute( "class", finalValue );
          }
        }
      }
    }

    return this;
  },

  toggleClass: function( value, stateVal ) {
    var type = typeof value;

    if ( typeof stateVal === "boolean" && type === "string" ) {
      return stateVal ? this.addClass( value ) : this.removeClass( value );
    }

    if ( jQuery.isFunction( value ) ) {
      return this.each( function( i ) {
        jQuery( this ).toggleClass(
          value.call( this, i, getClass( this ), stateVal ),
          stateVal
        );
      } );
    }

    return this.each( function() {
      var className, i, self, classNames;

      if ( type === "string" ) {

        // Toggle individual class names
        i = 0;
        self = jQuery( this );
        classNames = value.match( rnotwhite ) || [];

        while ( ( className = classNames[ i++ ] ) ) {

          // Check each className given, space separated list
          if ( self.hasClass( className ) ) {
            self.removeClass( className );
          } else {
            self.addClass( className );
          }
        }

      // Toggle whole class name
      } else if ( value === undefined || type === "boolean" ) {
        className = getClass( this );
        if ( className ) {

          // Store className if set
          dataPriv.set( this, "__className__", className );
        }

        // If the element has a class name or if we're passed `false`,
        // then remove the whole classname (if there was one, the above saved it).
        // Otherwise bring back whatever was previously saved (if anything),
        // falling back to the empty string if nothing was stored.
        if ( this.setAttribute ) {
          this.setAttribute( "class",
            className || value === false ?
            "" :
            dataPriv.get( this, "__className__" ) || ""
          );
        }
      }
    } );
  },

  hasClass: function( selector ) {
    var className, elem,
      i = 0;

    className = " " + selector + " ";
    while ( ( elem = this[ i++ ] ) ) {
      if ( elem.nodeType === 1 &&
        ( " " + getClass( elem ) + " " ).replace( rclass, " " )
          .indexOf( className ) > -1
      ) {
        return true;
      }
    }

    return false;
  }
} );




var rreturn = /\r/g,
  rspaces = /[\x20\t\r\n\f]+/g;

jQuery.fn.extend( {
  val: function( value ) {
    var hooks, ret, isFunction,
      elem = this[ 0 ];

    if ( !arguments.length ) {
      if ( elem ) {
        hooks = jQuery.valHooks[ elem.type ] ||
          jQuery.valHooks[ elem.nodeName.toLowerCase() ];

        if ( hooks &&
          "get" in hooks &&
          ( ret = hooks.get( elem, "value" ) ) !== undefined
        ) {
          return ret;
        }

        ret = elem.value;

        return typeof ret === "string" ?

          // Handle most common string cases
          ret.replace( rreturn, "" ) :

          // Handle cases where value is null/undef or number
          ret == null ? "" : ret;
      }

      return;
    }

    isFunction = jQuery.isFunction( value );

    return this.each( function( i ) {
      var val;

      if ( this.nodeType !== 1 ) {
        return;
      }

      if ( isFunction ) {
        val = value.call( this, i, jQuery( this ).val() );
      } else {
        val = value;
      }

      // Treat null/undefined as ""; convert numbers to string
      if ( val == null ) {
        val = "";

      } else if ( typeof val === "number" ) {
        val += "";

      } else if ( jQuery.isArray( val ) ) {
        val = jQuery.map( val, function( value ) {
          return value == null ? "" : value + "";
        } );
      }

      hooks = jQuery.valHooks[ this.type ] || jQuery.valHooks[ this.nodeName.toLowerCase() ];

      // If set returns undefined, fall back to normal setting
      if ( !hooks || !( "set" in hooks ) || hooks.set( this, val, "value" ) === undefined ) {
        this.value = val;
      }
    } );
  }
} );

jQuery.extend( {
  valHooks: {
    option: {
      get: function( elem ) {

        var val = jQuery.find.attr( elem, "value" );
        return val != null ?
          val :

          // Support: IE10-11+
          // option.text throws exceptions (#14686, #14858)
          // Strip and collapse whitespace
          // https://html.spec.whatwg.org/#strip-and-collapse-whitespace
          jQuery.trim( jQuery.text( elem ) ).replace( rspaces, " " );
      }
    },
    select: {
      get: function( elem ) {
        var value, option,
          options = elem.options,
          index = elem.selectedIndex,
          one = elem.type === "select-one" || index < 0,
          values = one ? null : [],
          max = one ? index + 1 : options.length,
          i = index < 0 ?
            max :
            one ? index : 0;

        // Loop through all the selected options
        for ( ; i < max; i++ ) {
          option = options[ i ];

          // IE8-9 doesn't update selected after form reset (#2551)
          if ( ( option.selected || i === index ) &&

              // Don't return options that are disabled or in a disabled optgroup
              ( support.optDisabled ?
                !option.disabled : option.getAttribute( "disabled" ) === null ) &&
              ( !option.parentNode.disabled ||
                !jQuery.nodeName( option.parentNode, "optgroup" ) ) ) {

            // Get the specific value for the option
            value = jQuery( option ).val();

            // We don't need an array for one selects
            if ( one ) {
              return value;
            }

            // Multi-Selects return an array
            values.push( value );
          }
        }

        return values;
      },

      set: function( elem, value ) {
        var optionSet, option,
          options = elem.options,
          values = jQuery.makeArray( value ),
          i = options.length;

        while ( i-- ) {
          option = options[ i ];
          if ( option.selected =
            jQuery.inArray( jQuery.valHooks.option.get( option ), values ) > -1
          ) {
            optionSet = true;
          }
        }

        // Force browsers to behave consistently when non-matching value is set
        if ( !optionSet ) {
          elem.selectedIndex = -1;
        }
        return values;
      }
    }
  }
} );

// Radios and checkboxes getter/setter
jQuery.each( [ "radio", "checkbox" ], function() {
  jQuery.valHooks[ this ] = {
    set: function( elem, value ) {
      if ( jQuery.isArray( value ) ) {
        return ( elem.checked = jQuery.inArray( jQuery( elem ).val(), value ) > -1 );
      }
    }
  };
  if ( !support.checkOn ) {
    jQuery.valHooks[ this ].get = function( elem ) {
      return elem.getAttribute( "value" ) === null ? "on" : elem.value;
    };
  }
} );




// Return jQuery for attributes-only inclusion


var rfocusMorph = /^(?:focusinfocus|focusoutblur)$/;

jQuery.extend( jQuery.event, {

  trigger: function( event, data, elem, onlyHandlers ) {

    var i, cur, tmp, bubbleType, ontype, handle, special,
      eventPath = [ elem || document ],
      type = hasOwn.call( event, "type" ) ? event.type : event,
      namespaces = hasOwn.call( event, "namespace" ) ? event.namespace.split( "." ) : [];

    cur = tmp = elem = elem || document;

    // Don't do events on text and comment nodes
    if ( elem.nodeType === 3 || elem.nodeType === 8 ) {
      return;
    }

    // focus/blur morphs to focusin/out; ensure we're not firing them right now
    if ( rfocusMorph.test( type + jQuery.event.triggered ) ) {
      return;
    }

    if ( type.indexOf( "." ) > -1 ) {

      // Namespaced trigger; create a regexp to match event type in handle()
      namespaces = type.split( "." );
      type = namespaces.shift();
      namespaces.sort();
    }
    ontype = type.indexOf( ":" ) < 0 && "on" + type;

    // Caller can pass in a jQuery.Event object, Object, or just an event type string
    event = event[ jQuery.expando ] ?
      event :
      new jQuery.Event( type, typeof event === "object" && event );

    // Trigger bitmask: & 1 for native handlers; & 2 for jQuery (always true)
    event.isTrigger = onlyHandlers ? 2 : 3;
    event.namespace = namespaces.join( "." );
    event.rnamespace = event.namespace ?
      new RegExp( "(^|\\.)" + namespaces.join( "\\.(?:.*\\.|)" ) + "(\\.|$)" ) :
      null;

    // Clean up the event in case it is being reused
    event.result = undefined;
    if ( !event.target ) {
      event.target = elem;
    }

    // Clone any incoming data and prepend the event, creating the handler arg list
    data = data == null ?
      [ event ] :
      jQuery.makeArray( data, [ event ] );

    // Allow special events to draw outside the lines
    special = jQuery.event.special[ type ] || {};
    if ( !onlyHandlers && special.trigger && special.trigger.apply( elem, data ) === false ) {
      return;
    }

    // Determine event propagation path in advance, per W3C events spec (#9951)
    // Bubble up to document, then to window; watch for a global ownerDocument var (#9724)
    if ( !onlyHandlers && !special.noBubble && !jQuery.isWindow( elem ) ) {

      bubbleType = special.delegateType || type;
      if ( !rfocusMorph.test( bubbleType + type ) ) {
        cur = cur.parentNode;
      }
      for ( ; cur; cur = cur.parentNode ) {
        eventPath.push( cur );
        tmp = cur;
      }

      // Only add window if we got to document (e.g., not plain obj or detached DOM)
      if ( tmp === ( elem.ownerDocument || document ) ) {
        eventPath.push( tmp.defaultView || tmp.parentWindow || window );
      }
    }

    // Fire handlers on the event path
    i = 0;
    while ( ( cur = eventPath[ i++ ] ) && !event.isPropagationStopped() ) {

      event.type = i > 1 ?
        bubbleType :
        special.bindType || type;

      // jQuery handler
      handle = ( dataPriv.get( cur, "events" ) || {} )[ event.type ] &&
        dataPriv.get( cur, "handle" );
      if ( handle ) {
        handle.apply( cur, data );
      }

      // Native handler
      handle = ontype && cur[ ontype ];
      if ( handle && handle.apply && acceptData( cur ) ) {
        event.result = handle.apply( cur, data );
        if ( event.result === false ) {
          event.preventDefault();
        }
      }
    }
    event.type = type;

    // If nobody prevented the default action, do it now
    if ( !onlyHandlers && !event.isDefaultPrevented() ) {

      if ( ( !special._default ||
        special._default.apply( eventPath.pop(), data ) === false ) &&
        acceptData( elem ) ) {

        // Call a native DOM method on the target with the same name name as the event.
        // Don't do default actions on window, that's where global variables be (#6170)
        if ( ontype && jQuery.isFunction( elem[ type ] ) && !jQuery.isWindow( elem ) ) {

          // Don't re-trigger an onFOO event when we call its FOO() method
          tmp = elem[ ontype ];

          if ( tmp ) {
            elem[ ontype ] = null;
          }

          // Prevent re-triggering of the same event, since we already bubbled it above
          jQuery.event.triggered = type;
          elem[ type ]();
          jQuery.event.triggered = undefined;

          if ( tmp ) {
            elem[ ontype ] = tmp;
          }
        }
      }
    }

    return event.result;
  },

  // Piggyback on a donor event to simulate a different one
  // Used only for `focus(in | out)` events
  simulate: function( type, elem, event ) {
    var e = jQuery.extend(
      new jQuery.Event(),
      event,
      {
        type: type,
        isSimulated: true
      }
    );

    jQuery.event.trigger( e, null, elem );
  }

} );

jQuery.fn.extend( {

  trigger: function( type, data ) {
    return this.each( function() {
      jQuery.event.trigger( type, data, this );
    } );
  },
  triggerHandler: function( type, data ) {
    var elem = this[ 0 ];
    if ( elem ) {
      return jQuery.event.trigger( type, data, elem, true );
    }
  }
} );


jQuery.each( ( "blur focus focusin focusout load resize scroll unload click dblclick " +
  "mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave " +
  "change select submit keydown keypress keyup error contextmenu" ).split( " " ),
  function( i, name ) {

  // Handle event binding
  jQuery.fn[ name ] = function( data, fn ) {
    return arguments.length > 0 ?
      this.on( name, null, data, fn ) :
      this.trigger( name );
  };
} );

jQuery.fn.extend( {
  hover: function( fnOver, fnOut ) {
    return this.mouseenter( fnOver ).mouseleave( fnOut || fnOver );
  }
} );




support.focusin = "onfocusin" in window;


// Support: Firefox
// Firefox doesn't have focus(in | out) events
// Related ticket - https://bugzilla.mozilla.org/show_bug.cgi?id=687787
//
// Support: Chrome, Safari
// focus(in | out) events fire after focus & blur events,
// which is spec violation - http://www.w3.org/TR/DOM-Level-3-Events/#events-focusevent-event-order
// Related ticket - https://code.google.com/p/chromium/issues/detail?id=449857
if ( !support.focusin ) {
  jQuery.each( { focus: "focusin", blur: "focusout" }, function( orig, fix ) {

    // Attach a single capturing handler on the document while someone wants focusin/focusout
    var handler = function( event ) {
      jQuery.event.simulate( fix, event.target, jQuery.event.fix( event ) );
    };

    jQuery.event.special[ fix ] = {
      setup: function() {
        var doc = this.ownerDocument || this,
          attaches = dataPriv.access( doc, fix );

        if ( !attaches ) {
          doc.addEventListener( orig, handler, true );
        }
        dataPriv.access( doc, fix, ( attaches || 0 ) + 1 );
      },
      teardown: function() {
        var doc = this.ownerDocument || this,
          attaches = dataPriv.access( doc, fix ) - 1;

        if ( !attaches ) {
          doc.removeEventListener( orig, handler, true );
          dataPriv.remove( doc, fix );

        } else {
          dataPriv.access( doc, fix, attaches );
        }
      }
    };
  } );
}
var location = window.location;

var nonce = jQuery.now();

var rquery = ( /\?/ );



// Support: Android 2.3
// Workaround failure to string-cast null input
jQuery.parseJSON = function( data ) {
  return JSON.parse( data + "" );
};


// Cross-browser xml parsing
jQuery.parseXML = function( data ) {
  var xml;
  if ( !data || typeof data !== "string" ) {
    return null;
  }

  // Support: IE9
  try {
    xml = ( new window.DOMParser() ).parseFromString( data, "text/xml" );
  } catch ( e ) {
    xml = undefined;
  }

  if ( !xml || xml.getElementsByTagName( "parsererror" ).length ) {
    jQuery.error( "Invalid XML: " + data );
  }
  return xml;
};


var
  rhash = /#.*$/,
  rts = /([?&])_=[^&]*/,
  rheaders = /^(.*?):[ \t]*([^\r\n]*)$/mg,

  // #7653, #8125, #8152: local protocol detection
  rlocalProtocol = /^(?:about|app|app-storage|.+-extension|file|res|widget):$/,
  rnoContent = /^(?:GET|HEAD)$/,
  rprotocol = /^\/\//,

  /* Prefilters
   * 1) They are useful to introduce custom dataTypes (see ajax/jsonp.js for an example)
   * 2) These are called:
   *    - BEFORE asking for a transport
   *    - AFTER param serialization (s.data is a string if s.processData is true)
   * 3) key is the dataType
   * 4) the catchall symbol "*" can be used
   * 5) execution will start with transport dataType and THEN continue down to "*" if needed
   */
  prefilters = {},

  /* Transports bindings
   * 1) key is the dataType
   * 2) the catchall symbol "*" can be used
   * 3) selection will start with transport dataType and THEN go to "*" if needed
   */
  transports = {},

  // Avoid comment-prolog char sequence (#10098); must appease lint and evade compression
  allTypes = "*/".concat( "*" ),

  // Anchor tag for parsing the document origin
  originAnchor = document.createElement( "a" );
  originAnchor.href = location.href;

// Base "constructor" for jQuery.ajaxPrefilter and jQuery.ajaxTransport
function addToPrefiltersOrTransports( structure ) {

  // dataTypeExpression is optional and defaults to "*"
  return function( dataTypeExpression, func ) {

    if ( typeof dataTypeExpression !== "string" ) {
      func = dataTypeExpression;
      dataTypeExpression = "*";
    }

    var dataType,
      i = 0,
      dataTypes = dataTypeExpression.toLowerCase().match( rnotwhite ) || [];

    if ( jQuery.isFunction( func ) ) {

      // For each dataType in the dataTypeExpression
      while ( ( dataType = dataTypes[ i++ ] ) ) {

        // Prepend if requested
        if ( dataType[ 0 ] === "+" ) {
          dataType = dataType.slice( 1 ) || "*";
          ( structure[ dataType ] = structure[ dataType ] || [] ).unshift( func );

        // Otherwise append
        } else {
          ( structure[ dataType ] = structure[ dataType ] || [] ).push( func );
        }
      }
    }
  };
}

// Base inspection function for prefilters and transports
function inspectPrefiltersOrTransports( structure, options, originalOptions, jqXHR ) {

  var inspected = {},
    seekingTransport = ( structure === transports );

  function inspect( dataType ) {
    var selected;
    inspected[ dataType ] = true;
    jQuery.each( structure[ dataType ] || [], function( _, prefilterOrFactory ) {
      var dataTypeOrTransport = prefilterOrFactory( options, originalOptions, jqXHR );
      if ( typeof dataTypeOrTransport === "string" &&
        !seekingTransport && !inspected[ dataTypeOrTransport ] ) {

        options.dataTypes.unshift( dataTypeOrTransport );
        inspect( dataTypeOrTransport );
        return false;
      } else if ( seekingTransport ) {
        return !( selected = dataTypeOrTransport );
      }
    } );
    return selected;
  }

  return inspect( options.dataTypes[ 0 ] ) || !inspected[ "*" ] && inspect( "*" );
}

// A special extend for ajax options
// that takes "flat" options (not to be deep extended)
// Fixes #9887
function ajaxExtend( target, src ) {
  var key, deep,
    flatOptions = jQuery.ajaxSettings.flatOptions || {};

  for ( key in src ) {
    if ( src[ key ] !== undefined ) {
      ( flatOptions[ key ] ? target : ( deep || ( deep = {} ) ) )[ key ] = src[ key ];
    }
  }
  if ( deep ) {
    jQuery.extend( true, target, deep );
  }

  return target;
}

/* Handles responses to an ajax request:
 * - finds the right dataType (mediates between content-type and expected dataType)
 * - returns the corresponding response
 */
function ajaxHandleResponses( s, jqXHR, responses ) {

  var ct, type, finalDataType, firstDataType,
    contents = s.contents,
    dataTypes = s.dataTypes;

  // Remove auto dataType and get content-type in the process
  while ( dataTypes[ 0 ] === "*" ) {
    dataTypes.shift();
    if ( ct === undefined ) {
      ct = s.mimeType || jqXHR.getResponseHeader( "Content-Type" );
    }
  }

  // Check if we're dealing with a known content-type
  if ( ct ) {
    for ( type in contents ) {
      if ( contents[ type ] && contents[ type ].test( ct ) ) {
        dataTypes.unshift( type );
        break;
      }
    }
  }

  // Check to see if we have a response for the expected dataType
  if ( dataTypes[ 0 ] in responses ) {
    finalDataType = dataTypes[ 0 ];
  } else {

    // Try convertible dataTypes
    for ( type in responses ) {
      if ( !dataTypes[ 0 ] || s.converters[ type + " " + dataTypes[ 0 ] ] ) {
        finalDataType = type;
        break;
      }
      if ( !firstDataType ) {
        firstDataType = type;
      }
    }

    // Or just use first one
    finalDataType = finalDataType || firstDataType;
  }

  // If we found a dataType
  // We add the dataType to the list if needed
  // and return the corresponding response
  if ( finalDataType ) {
    if ( finalDataType !== dataTypes[ 0 ] ) {
      dataTypes.unshift( finalDataType );
    }
    return responses[ finalDataType ];
  }
}

/* Chain conversions given the request and the original response
 * Also sets the responseXXX fields on the jqXHR instance
 */
function ajaxConvert( s, response, jqXHR, isSuccess ) {
  var conv2, current, conv, tmp, prev,
    converters = {},

    // Work with a copy of dataTypes in case we need to modify it for conversion
    dataTypes = s.dataTypes.slice();

  // Create converters map with lowercased keys
  if ( dataTypes[ 1 ] ) {
    for ( conv in s.converters ) {
      converters[ conv.toLowerCase() ] = s.converters[ conv ];
    }
  }

  current = dataTypes.shift();

  // Convert to each sequential dataType
  while ( current ) {

    if ( s.responseFields[ current ] ) {
      jqXHR[ s.responseFields[ current ] ] = response;
    }

    // Apply the dataFilter if provided
    if ( !prev && isSuccess && s.dataFilter ) {
      response = s.dataFilter( response, s.dataType );
    }

    prev = current;
    current = dataTypes.shift();

    if ( current ) {

    // There's only work to do if current dataType is non-auto
      if ( current === "*" ) {

        current = prev;

      // Convert response if prev dataType is non-auto and differs from current
      } else if ( prev !== "*" && prev !== current ) {

        // Seek a direct converter
        conv = converters[ prev + " " + current ] || converters[ "* " + current ];

        // If none found, seek a pair
        if ( !conv ) {
          for ( conv2 in converters ) {

            // If conv2 outputs current
            tmp = conv2.split( " " );
            if ( tmp[ 1 ] === current ) {

              // If prev can be converted to accepted input
              conv = converters[ prev + " " + tmp[ 0 ] ] ||
                converters[ "* " + tmp[ 0 ] ];
              if ( conv ) {

                // Condense equivalence converters
                if ( conv === true ) {
                  conv = converters[ conv2 ];

                // Otherwise, insert the intermediate dataType
                } else if ( converters[ conv2 ] !== true ) {
                  current = tmp[ 0 ];
                  dataTypes.unshift( tmp[ 1 ] );
                }
                break;
              }
            }
          }
        }

        // Apply converter (if not an equivalence)
        if ( conv !== true ) {

          // Unless errors are allowed to bubble, catch and return them
          if ( conv && s.throws ) {
            response = conv( response );
          } else {
            try {
              response = conv( response );
            } catch ( e ) {
              return {
                state: "parsererror",
                error: conv ? e : "No conversion from " + prev + " to " + current
              };
            }
          }
        }
      }
    }
  }

  return { state: "success", data: response };
}

jQuery.extend( {

  // Counter for holding the number of active queries
  active: 0,

  // Last-Modified header cache for next request
  lastModified: {},
  etag: {},

  ajaxSettings: {
    url: location.href,
    type: "GET",
    isLocal: rlocalProtocol.test( location.protocol ),
    global: true,
    processData: true,
    async: true,
    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    /*
    timeout: 0,
    data: null,
    dataType: null,
    username: null,
    password: null,
    cache: null,
    throws: false,
    traditional: false,
    headers: {},
    */

    accepts: {
      "*": allTypes,
      text: "text/plain",
      html: "text/html",
      xml: "application/xml, text/xml",
      json: "application/json, text/javascript"
    },

    contents: {
      xml: /\bxml\b/,
      html: /\bhtml/,
      json: /\bjson\b/
    },

    responseFields: {
      xml: "responseXML",
      text: "responseText",
      json: "responseJSON"
    },

    // Data converters
    // Keys separate source (or catchall "*") and destination types with a single space
    converters: {

      // Convert anything to text
      "* text": String,

      // Text to html (true = no transformation)
      "text html": true,

      // Evaluate text as a json expression
      "text json": jQuery.parseJSON,

      // Parse text as xml
      "text xml": jQuery.parseXML
    },

    // For options that shouldn't be deep extended:
    // you can add your own custom options here if
    // and when you create one that shouldn't be
    // deep extended (see ajaxExtend)
    flatOptions: {
      url: true,
      context: true
    }
  },

  // Creates a full fledged settings object into target
  // with both ajaxSettings and settings fields.
  // If target is omitted, writes into ajaxSettings.
  ajaxSetup: function( target, settings ) {
    return settings ?

      // Building a settings object
      ajaxExtend( ajaxExtend( target, jQuery.ajaxSettings ), settings ) :

      // Extending ajaxSettings
      ajaxExtend( jQuery.ajaxSettings, target );
  },

  ajaxPrefilter: addToPrefiltersOrTransports( prefilters ),
  ajaxTransport: addToPrefiltersOrTransports( transports ),

  // Main method
  ajax: function( url, options ) {

    // If url is an object, simulate pre-1.5 signature
    if ( typeof url === "object" ) {
      options = url;
      url = undefined;
    }

    // Force options to be an object
    options = options || {};

    var transport,

      // URL without anti-cache param
      cacheURL,

      // Response headers
      responseHeadersString,
      responseHeaders,

      // timeout handle
      timeoutTimer,

      // Url cleanup var
      urlAnchor,

      // To know if global events are to be dispatched
      fireGlobals,

      // Loop variable
      i,

      // Create the final options object
      s = jQuery.ajaxSetup( {}, options ),

      // Callbacks context
      callbackContext = s.context || s,

      // Context for global events is callbackContext if it is a DOM node or jQuery collection
      globalEventContext = s.context &&
        ( callbackContext.nodeType || callbackContext.jquery ) ?
          jQuery( callbackContext ) :
          jQuery.event,

      // Deferreds
      deferred = jQuery.Deferred(),
      completeDeferred = jQuery.Callbacks( "once memory" ),

      // Status-dependent callbacks
      statusCode = s.statusCode || {},

      // Headers (they are sent all at once)
      requestHeaders = {},
      requestHeadersNames = {},

      // The jqXHR state
      state = 0,

      // Default abort message
      strAbort = "canceled",

      // Fake xhr
      jqXHR = {
        readyState: 0,

        // Builds headers hashtable if needed
        getResponseHeader: function( key ) {
          var match;
          if ( state === 2 ) {
            if ( !responseHeaders ) {
              responseHeaders = {};
              while ( ( match = rheaders.exec( responseHeadersString ) ) ) {
                responseHeaders[ match[ 1 ].toLowerCase() ] = match[ 2 ];
              }
            }
            match = responseHeaders[ key.toLowerCase() ];
          }
          return match == null ? null : match;
        },

        // Raw string
        getAllResponseHeaders: function() {
          return state === 2 ? responseHeadersString : null;
        },

        // Caches the header
        setRequestHeader: function( name, value ) {
          var lname = name.toLowerCase();
          if ( !state ) {
            name = requestHeadersNames[ lname ] = requestHeadersNames[ lname ] || name;
            requestHeaders[ name ] = value;
          }
          return this;
        },

        // Overrides response content-type header
        overrideMimeType: function( type ) {
          if ( !state ) {
            s.mimeType = type;
          }
          return this;
        },

        // Status-dependent callbacks
        statusCode: function( map ) {
          var code;
          if ( map ) {
            if ( state < 2 ) {
              for ( code in map ) {

                // Lazy-add the new callback in a way that preserves old ones
                statusCode[ code ] = [ statusCode[ code ], map[ code ] ];
              }
            } else {

              // Execute the appropriate callbacks
              jqXHR.always( map[ jqXHR.status ] );
            }
          }
          return this;
        },

        // Cancel the request
        abort: function( statusText ) {
          var finalText = statusText || strAbort;
          if ( transport ) {
            transport.abort( finalText );
          }
          done( 0, finalText );
          return this;
        }
      };

    // Attach deferreds
    deferred.promise( jqXHR ).complete = completeDeferred.add;
    jqXHR.success = jqXHR.done;
    jqXHR.error = jqXHR.fail;

    // Remove hash character (#7531: and string promotion)
    // Add protocol if not provided (prefilters might expect it)
    // Handle falsy url in the settings object (#10093: consistency with old signature)
    // We also use the url parameter if available
    s.url = ( ( url || s.url || location.href ) + "" ).replace( rhash, "" )
      .replace( rprotocol, location.protocol + "//" );

    // Alias method option to type as per ticket #12004
    s.type = options.method || options.type || s.method || s.type;

    // Extract dataTypes list
    s.dataTypes = jQuery.trim( s.dataType || "*" ).toLowerCase().match( rnotwhite ) || [ "" ];

    // A cross-domain request is in order when the origin doesn't match the current origin.
    if ( s.crossDomain == null ) {
      urlAnchor = document.createElement( "a" );

      // Support: IE8-11+
      // IE throws exception if url is malformed, e.g. http://example.com:80x/
      try {
        urlAnchor.href = s.url;

        // Support: IE8-11+
        // Anchor's host property isn't correctly set when s.url is relative
        urlAnchor.href = urlAnchor.href;
        s.crossDomain = originAnchor.protocol + "//" + originAnchor.host !==
          urlAnchor.protocol + "//" + urlAnchor.host;
      } catch ( e ) {

        // If there is an error parsing the URL, assume it is crossDomain,
        // it can be rejected by the transport if it is invalid
        s.crossDomain = true;
      }
    }

    // Convert data if not already a string
    if ( s.data && s.processData && typeof s.data !== "string" ) {
      s.data = jQuery.param( s.data, s.traditional );
    }

    // Apply prefilters
    inspectPrefiltersOrTransports( prefilters, s, options, jqXHR );

    // If request was aborted inside a prefilter, stop there
    if ( state === 2 ) {
      return jqXHR;
    }

    // We can fire global events as of now if asked to
    // Don't fire events if jQuery.event is undefined in an AMD-usage scenario (#15118)
    fireGlobals = jQuery.event && s.global;

    // Watch for a new set of requests
    if ( fireGlobals && jQuery.active++ === 0 ) {
      jQuery.event.trigger( "ajaxStart" );
    }

    // Uppercase the type
    s.type = s.type.toUpperCase();

    // Determine if request has content
    s.hasContent = !rnoContent.test( s.type );

    // Save the URL in case we're toying with the If-Modified-Since
    // and/or If-None-Match header later on
    cacheURL = s.url;

    // More options handling for requests with no content
    if ( !s.hasContent ) {

      // If data is available, append data to url
      if ( s.data ) {
        cacheURL = ( s.url += ( rquery.test( cacheURL ) ? "&" : "?" ) + s.data );

        // #9682: remove data so that it's not used in an eventual retry
        delete s.data;
      }

      // Add anti-cache in url if needed
      if ( s.cache === false ) {
        s.url = rts.test( cacheURL ) ?

          // If there is already a '_' parameter, set its value
          cacheURL.replace( rts, "$1_=" + nonce++ ) :

          // Otherwise add one to the end
          cacheURL + ( rquery.test( cacheURL ) ? "&" : "?" ) + "_=" + nonce++;
      }
    }

    // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
    if ( s.ifModified ) {
      if ( jQuery.lastModified[ cacheURL ] ) {
        jqXHR.setRequestHeader( "If-Modified-Since", jQuery.lastModified[ cacheURL ] );
      }
      if ( jQuery.etag[ cacheURL ] ) {
        jqXHR.setRequestHeader( "If-None-Match", jQuery.etag[ cacheURL ] );
      }
    }

    // Set the correct header, if data is being sent
    if ( s.data && s.hasContent && s.contentType !== false || options.contentType ) {
      jqXHR.setRequestHeader( "Content-Type", s.contentType );
    }

    // Set the Accepts header for the server, depending on the dataType
    jqXHR.setRequestHeader(
      "Accept",
      s.dataTypes[ 0 ] && s.accepts[ s.dataTypes[ 0 ] ] ?
        s.accepts[ s.dataTypes[ 0 ] ] +
          ( s.dataTypes[ 0 ] !== "*" ? ", " + allTypes + "; q=0.01" : "" ) :
        s.accepts[ "*" ]
    );

    // Check for headers option
    for ( i in s.headers ) {
      jqXHR.setRequestHeader( i, s.headers[ i ] );
    }

    // Allow custom headers/mimetypes and early abort
    if ( s.beforeSend &&
      ( s.beforeSend.call( callbackContext, jqXHR, s ) === false || state === 2 ) ) {

      // Abort if not done already and return
      return jqXHR.abort();
    }

    // Aborting is no longer a cancellation
    strAbort = "abort";

    // Install callbacks on deferreds
    for ( i in { success: 1, error: 1, complete: 1 } ) {
      jqXHR[ i ]( s[ i ] );
    }

    // Get transport
    transport = inspectPrefiltersOrTransports( transports, s, options, jqXHR );

    // If no transport, we auto-abort
    if ( !transport ) {
      done( -1, "No Transport" );
    } else {
      jqXHR.readyState = 1;

      // Send global event
      if ( fireGlobals ) {
        globalEventContext.trigger( "ajaxSend", [ jqXHR, s ] );
      }

      // If request was aborted inside ajaxSend, stop there
      if ( state === 2 ) {
        return jqXHR;
      }

      // Timeout
      if ( s.async && s.timeout > 0 ) {
        timeoutTimer = window.setTimeout( function() {
          jqXHR.abort( "timeout" );
        }, s.timeout );
      }

      try {
        state = 1;
        transport.send( requestHeaders, done );
      } catch ( e ) {

        // Propagate exception as error if not done
        if ( state < 2 ) {
          done( -1, e );

        // Simply rethrow otherwise
        } else {
          throw e;
        }
      }
    }

    // Callback for when everything is done
    function done( status, nativeStatusText, responses, headers ) {
      var isSuccess, success, error, response, modified,
        statusText = nativeStatusText;

      // Called once
      if ( state === 2 ) {
        return;
      }

      // State is "done" now
      state = 2;

      // Clear timeout if it exists
      if ( timeoutTimer ) {
        window.clearTimeout( timeoutTimer );
      }

      // Dereference transport for early garbage collection
      // (no matter how long the jqXHR object will be used)
      transport = undefined;

      // Cache response headers
      responseHeadersString = headers || "";

      // Set readyState
      jqXHR.readyState = status > 0 ? 4 : 0;

      // Determine if successful
      isSuccess = status >= 200 && status < 300 || status === 304;

      // Get response data
      if ( responses ) {
        response = ajaxHandleResponses( s, jqXHR, responses );
      }

      // Convert no matter what (that way responseXXX fields are always set)
      response = ajaxConvert( s, response, jqXHR, isSuccess );

      // If successful, handle type chaining
      if ( isSuccess ) {

        // Set the If-Modified-Since and/or If-None-Match header, if in ifModified mode.
        if ( s.ifModified ) {
          modified = jqXHR.getResponseHeader( "Last-Modified" );
          if ( modified ) {
            jQuery.lastModified[ cacheURL ] = modified;
          }
          modified = jqXHR.getResponseHeader( "etag" );
          if ( modified ) {
            jQuery.etag[ cacheURL ] = modified;
          }
        }

        // if no content
        if ( status === 204 || s.type === "HEAD" ) {
          statusText = "nocontent";

        // if not modified
        } else if ( status === 304 ) {
          statusText = "notmodified";

        // If we have data, let's convert it
        } else {
          statusText = response.state;
          success = response.data;
          error = response.error;
          isSuccess = !error;
        }
      } else {

        // Extract error from statusText and normalize for non-aborts
        error = statusText;
        if ( status || !statusText ) {
          statusText = "error";
          if ( status < 0 ) {
            status = 0;
          }
        }
      }

      // Set data for the fake xhr object
      jqXHR.status = status;
      jqXHR.statusText = ( nativeStatusText || statusText ) + "";

      // Success/Error
      if ( isSuccess ) {
        deferred.resolveWith( callbackContext, [ success, statusText, jqXHR ] );
      } else {
        deferred.rejectWith( callbackContext, [ jqXHR, statusText, error ] );
      }

      // Status-dependent callbacks
      jqXHR.statusCode( statusCode );
      statusCode = undefined;

      if ( fireGlobals ) {
        globalEventContext.trigger( isSuccess ? "ajaxSuccess" : "ajaxError",
          [ jqXHR, s, isSuccess ? success : error ] );
      }

      // Complete
      completeDeferred.fireWith( callbackContext, [ jqXHR, statusText ] );

      if ( fireGlobals ) {
        globalEventContext.trigger( "ajaxComplete", [ jqXHR, s ] );

        // Handle the global AJAX counter
        if ( !( --jQuery.active ) ) {
          jQuery.event.trigger( "ajaxStop" );
        }
      }
    }

    return jqXHR;
  },

  getJSON: function( url, data, callback ) {
    return jQuery.get( url, data, callback, "json" );
  },

  getScript: function( url, callback ) {
    return jQuery.get( url, undefined, callback, "script" );
  }
} );

jQuery.each( [ "get", "post" ], function( i, method ) {
  jQuery[ method ] = function( url, data, callback, type ) {

    // Shift arguments if data argument was omitted
    if ( jQuery.isFunction( data ) ) {
      type = type || callback;
      callback = data;
      data = undefined;
    }

    // The url can be an options object (which then must have .url)
    return jQuery.ajax( jQuery.extend( {
      url: url,
      type: method,
      dataType: type,
      data: data,
      success: callback
    }, jQuery.isPlainObject( url ) && url ) );
  };
} );


jQuery._evalUrl = function( url ) {
  return jQuery.ajax( {
    url: url,

    // Make this explicit, since user can override this through ajaxSetup (#11264)
    type: "GET",
    dataType: "script",
    async: false,
    global: false,
    "throws": true
  } );
};


jQuery.fn.extend( {
  wrapAll: function( html ) {
    var wrap;

    if ( jQuery.isFunction( html ) ) {
      return this.each( function( i ) {
        jQuery( this ).wrapAll( html.call( this, i ) );
      } );
    }

    if ( this[ 0 ] ) {

      // The elements to wrap the target around
      wrap = jQuery( html, this[ 0 ].ownerDocument ).eq( 0 ).clone( true );

      if ( this[ 0 ].parentNode ) {
        wrap.insertBefore( this[ 0 ] );
      }

      wrap.map( function() {
        var elem = this;

        while ( elem.firstElementChild ) {
          elem = elem.firstElementChild;
        }

        return elem;
      } ).append( this );
    }

    return this;
  },

  wrapInner: function( html ) {
    if ( jQuery.isFunction( html ) ) {
      return this.each( function( i ) {
        jQuery( this ).wrapInner( html.call( this, i ) );
      } );
    }

    return this.each( function() {
      var self = jQuery( this ),
        contents = self.contents();

      if ( contents.length ) {
        contents.wrapAll( html );

      } else {
        self.append( html );
      }
    } );
  },

  wrap: function( html ) {
    var isFunction = jQuery.isFunction( html );

    return this.each( function( i ) {
      jQuery( this ).wrapAll( isFunction ? html.call( this, i ) : html );
    } );
  },

  unwrap: function() {
    return this.parent().each( function() {
      if ( !jQuery.nodeName( this, "body" ) ) {
        jQuery( this ).replaceWith( this.childNodes );
      }
    } ).end();
  }
} );


jQuery.expr.filters.hidden = function( elem ) {
  return !jQuery.expr.filters.visible( elem );
};
jQuery.expr.filters.visible = function( elem ) {

  // Support: Opera <= 12.12
  // Opera reports offsetWidths and offsetHeights less than zero on some elements
  // Use OR instead of AND as the element is not visible if either is true
  // See tickets #10406 and #13132
  return elem.offsetWidth > 0 || elem.offsetHeight > 0 || elem.getClientRects().length > 0;
};




var r20 = /%20/g,
  rbracket = /\[\]$/,
  rCRLF = /\r?\n/g,
  rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
  rsubmittable = /^(?:input|select|textarea|keygen)/i;

function buildParams( prefix, obj, traditional, add ) {
  var name;

  if ( jQuery.isArray( obj ) ) {

    // Serialize array item.
    jQuery.each( obj, function( i, v ) {
      if ( traditional || rbracket.test( prefix ) ) {

        // Treat each array item as a scalar.
        add( prefix, v );

      } else {

        // Item is non-scalar (array or object), encode its numeric index.
        buildParams(
          prefix + "[" + ( typeof v === "object" && v != null ? i : "" ) + "]",
          v,
          traditional,
          add
        );
      }
    } );

  } else if ( !traditional && jQuery.type( obj ) === "object" ) {

    // Serialize object item.
    for ( name in obj ) {
      buildParams( prefix + "[" + name + "]", obj[ name ], traditional, add );
    }

  } else {

    // Serialize scalar item.
    add( prefix, obj );
  }
}

// Serialize an array of form elements or a set of
// key/values into a query string
jQuery.param = function( a, traditional ) {
  var prefix,
    s = [],
    add = function( key, value ) {

      // If value is a function, invoke it and return its value
      value = jQuery.isFunction( value ) ? value() : ( value == null ? "" : value );
      s[ s.length ] = encodeURIComponent( key ) + "=" + encodeURIComponent( value );
    };

  // Set traditional to true for jQuery <= 1.3.2 behavior.
  if ( traditional === undefined ) {
    traditional = jQuery.ajaxSettings && jQuery.ajaxSettings.traditional;
  }

  // If an array was passed in, assume that it is an array of form elements.
  if ( jQuery.isArray( a ) || ( a.jquery && !jQuery.isPlainObject( a ) ) ) {

    // Serialize the form elements
    jQuery.each( a, function() {
      add( this.name, this.value );
    } );

  } else {

    // If traditional, encode the "old" way (the way 1.3.2 or older
    // did it), otherwise encode params recursively.
    for ( prefix in a ) {
      buildParams( prefix, a[ prefix ], traditional, add );
    }
  }

  // Return the resulting serialization
  return s.join( "&" ).replace( r20, "+" );
};

jQuery.fn.extend( {
  serialize: function() {
    return jQuery.param( this.serializeArray() );
  },
  serializeArray: function() {
    return this.map( function() {

      // Can add propHook for "elements" to filter or add form elements
      var elements = jQuery.prop( this, "elements" );
      return elements ? jQuery.makeArray( elements ) : this;
    } )
    .filter( function() {
      var type = this.type;

      // Use .is( ":disabled" ) so that fieldset[disabled] works
      return this.name && !jQuery( this ).is( ":disabled" ) &&
        rsubmittable.test( this.nodeName ) && !rsubmitterTypes.test( type ) &&
        ( this.checked || !rcheckableType.test( type ) );
    } )
    .map( function( i, elem ) {
      var val = jQuery( this ).val();

      return val == null ?
        null :
        jQuery.isArray( val ) ?
          jQuery.map( val, function( val ) {
            return { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
          } ) :
          { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
    } ).get();
  }
} );


jQuery.ajaxSettings.xhr = function() {
  try {
    return new window.XMLHttpRequest();
  } catch ( e ) {}
};

var xhrSuccessStatus = {

    // File protocol always yields status code 0, assume 200
    0: 200,

    // Support: IE9
    // #1450: sometimes IE returns 1223 when it should be 204
    1223: 204
  },
  xhrSupported = jQuery.ajaxSettings.xhr();

support.cors = !!xhrSupported && ( "withCredentials" in xhrSupported );
support.ajax = xhrSupported = !!xhrSupported;

jQuery.ajaxTransport( function( options ) {
  var callback, errorCallback;

  // Cross domain only allowed if supported through XMLHttpRequest
  if ( support.cors || xhrSupported && !options.crossDomain ) {
    return {
      send: function( headers, complete ) {
        var i,
          xhr = options.xhr();

        xhr.open(
          options.type,
          options.url,
          options.async,
          options.username,
          options.password
        );

        // Apply custom fields if provided
        if ( options.xhrFields ) {
          for ( i in options.xhrFields ) {
            xhr[ i ] = options.xhrFields[ i ];
          }
        }

        // Override mime type if needed
        if ( options.mimeType && xhr.overrideMimeType ) {
          xhr.overrideMimeType( options.mimeType );
        }

        // X-Requested-With header
        // For cross-domain requests, seeing as conditions for a preflight are
        // akin to a jigsaw puzzle, we simply never set it to be sure.
        // (it can always be set on a per-request basis or even using ajaxSetup)
        // For same-domain requests, won't change header if already provided.
        if ( !options.crossDomain && !headers[ "X-Requested-With" ] ) {
          headers[ "X-Requested-With" ] = "XMLHttpRequest";
        }

        // Set headers
        for ( i in headers ) {
          xhr.setRequestHeader( i, headers[ i ] );
        }

        // Callback
        callback = function( type ) {
          return function() {
            if ( callback ) {
              callback = errorCallback = xhr.onload =
                xhr.onerror = xhr.onabort = xhr.onreadystatechange = null;

              if ( type === "abort" ) {
                xhr.abort();
              } else if ( type === "error" ) {

                // Support: IE9
                // On a manual native abort, IE9 throws
                // errors on any property access that is not readyState
                if ( typeof xhr.status !== "number" ) {
                  complete( 0, "error" );
                } else {
                  complete(

                    // File: protocol always yields status 0; see #8605, #14207
                    xhr.status,
                    xhr.statusText
                  );
                }
              } else {
                complete(
                  xhrSuccessStatus[ xhr.status ] || xhr.status,
                  xhr.statusText,

                  // Support: IE9 only
                  // IE9 has no XHR2 but throws on binary (trac-11426)
                  // For XHR2 non-text, let the caller handle it (gh-2498)
                  ( xhr.responseType || "text" ) !== "text"  ||
                  typeof xhr.responseText !== "string" ?
                    { binary: xhr.response } :
                    { text: xhr.responseText },
                  xhr.getAllResponseHeaders()
                );
              }
            }
          };
        };

        // Listen to events
        xhr.onload = callback();
        errorCallback = xhr.onerror = callback( "error" );

        // Support: IE9
        // Use onreadystatechange to replace onabort
        // to handle uncaught aborts
        if ( xhr.onabort !== undefined ) {
          xhr.onabort = errorCallback;
        } else {
          xhr.onreadystatechange = function() {

            // Check readyState before timeout as it changes
            if ( xhr.readyState === 4 ) {

              // Allow onerror to be called first,
              // but that will not handle a native abort
              // Also, save errorCallback to a variable
              // as xhr.onerror cannot be accessed
              window.setTimeout( function() {
                if ( callback ) {
                  errorCallback();
                }
              } );
            }
          };
        }

        // Create the abort callback
        callback = callback( "abort" );

        try {

          // Do send the request (this may raise an exception)
          xhr.send( options.hasContent && options.data || null );
        } catch ( e ) {

          // #14683: Only rethrow if this hasn't been notified as an error yet
          if ( callback ) {
            throw e;
          }
        }
      },

      abort: function() {
        if ( callback ) {
          callback();
        }
      }
    };
  }
} );




// Install script dataType
jQuery.ajaxSetup( {
  accepts: {
    script: "text/javascript, application/javascript, " +
      "application/ecmascript, application/x-ecmascript"
  },
  contents: {
    script: /\b(?:java|ecma)script\b/
  },
  converters: {
    "text script": function( text ) {
      jQuery.globalEval( text );
      return text;
    }
  }
} );

// Handle cache's special case and crossDomain
jQuery.ajaxPrefilter( "script", function( s ) {
  if ( s.cache === undefined ) {
    s.cache = false;
  }
  if ( s.crossDomain ) {
    s.type = "GET";
  }
} );

// Bind script tag hack transport
jQuery.ajaxTransport( "script", function( s ) {

  // This transport only deals with cross domain requests
  if ( s.crossDomain ) {
    var script, callback;
    return {
      send: function( _, complete ) {
        script = jQuery( "<script>" ).prop( {
          charset: s.scriptCharset,
          src: s.url
        } ).on(
          "load error",
          callback = function( evt ) {
            script.remove();
            callback = null;
            if ( evt ) {
              complete( evt.type === "error" ? 404 : 200, evt.type );
            }
          }
        );

        // Use native DOM manipulation to avoid our domManip AJAX trickery
        document.head.appendChild( script[ 0 ] );
      },
      abort: function() {
        if ( callback ) {
          callback();
        }
      }
    };
  }
} );




var oldCallbacks = [],
  rjsonp = /(=)\?(?=&|$)|\?\?/;

// Default jsonp settings
jQuery.ajaxSetup( {
  jsonp: "callback",
  jsonpCallback: function() {
    var callback = oldCallbacks.pop() || ( jQuery.expando + "_" + ( nonce++ ) );
    this[ callback ] = true;
    return callback;
  }
} );

// Detect, normalize options and install callbacks for jsonp requests
jQuery.ajaxPrefilter( "json jsonp", function( s, originalSettings, jqXHR ) {

  var callbackName, overwritten, responseContainer,
    jsonProp = s.jsonp !== false && ( rjsonp.test( s.url ) ?
      "url" :
      typeof s.data === "string" &&
        ( s.contentType || "" )
          .indexOf( "application/x-www-form-urlencoded" ) === 0 &&
        rjsonp.test( s.data ) && "data"
    );

  // Handle iff the expected data type is "jsonp" or we have a parameter to set
  if ( jsonProp || s.dataTypes[ 0 ] === "jsonp" ) {

    // Get callback name, remembering preexisting value associated with it
    callbackName = s.jsonpCallback = jQuery.isFunction( s.jsonpCallback ) ?
      s.jsonpCallback() :
      s.jsonpCallback;

    // Insert callback into url or form data
    if ( jsonProp ) {
      s[ jsonProp ] = s[ jsonProp ].replace( rjsonp, "$1" + callbackName );
    } else if ( s.jsonp !== false ) {
      s.url += ( rquery.test( s.url ) ? "&" : "?" ) + s.jsonp + "=" + callbackName;
    }

    // Use data converter to retrieve json after script execution
    s.converters[ "script json" ] = function() {
      if ( !responseContainer ) {
        jQuery.error( callbackName + " was not called" );
      }
      return responseContainer[ 0 ];
    };

    // Force json dataType
    s.dataTypes[ 0 ] = "json";

    // Install callback
    overwritten = window[ callbackName ];
    window[ callbackName ] = function() {
      responseContainer = arguments;
    };

    // Clean-up function (fires after converters)
    jqXHR.always( function() {

      // If previous value didn't exist - remove it
      if ( overwritten === undefined ) {
        jQuery( window ).removeProp( callbackName );

      // Otherwise restore preexisting value
      } else {
        window[ callbackName ] = overwritten;
      }

      // Save back as free
      if ( s[ callbackName ] ) {

        // Make sure that re-using the options doesn't screw things around
        s.jsonpCallback = originalSettings.jsonpCallback;

        // Save the callback name for future use
        oldCallbacks.push( callbackName );
      }

      // Call if it was a function and we have a response
      if ( responseContainer && jQuery.isFunction( overwritten ) ) {
        overwritten( responseContainer[ 0 ] );
      }

      responseContainer = overwritten = undefined;
    } );

    // Delegate to script
    return "script";
  }
} );




// Argument "data" should be string of html
// context (optional): If specified, the fragment will be created in this context,
// defaults to document
// keepScripts (optional): If true, will include scripts passed in the html string
jQuery.parseHTML = function( data, context, keepScripts ) {
  if ( !data || typeof data !== "string" ) {
    return null;
  }
  if ( typeof context === "boolean" ) {
    keepScripts = context;
    context = false;
  }
  context = context || document;

  var parsed = rsingleTag.exec( data ),
    scripts = !keepScripts && [];

  // Single tag
  if ( parsed ) {
    return [ context.createElement( parsed[ 1 ] ) ];
  }

  parsed = buildFragment( [ data ], context, scripts );

  if ( scripts && scripts.length ) {
    jQuery( scripts ).remove();
  }

  return jQuery.merge( [], parsed.childNodes );
};


// Keep a copy of the old load method
var _load = jQuery.fn.load;

/**
 * Load a url into a page
 */
jQuery.fn.load = function( url, params, callback ) {
  if ( typeof url !== "string" && _load ) {
    return _load.apply( this, arguments );
  }

  var selector, type, response,
    self = this,
    off = url.indexOf( " " );

  if ( off > -1 ) {
    selector = jQuery.trim( url.slice( off ) );
    url = url.slice( 0, off );
  }

  // If it's a function
  if ( jQuery.isFunction( params ) ) {

    // We assume that it's the callback
    callback = params;
    params = undefined;

  // Otherwise, build a param string
  } else if ( params && typeof params === "object" ) {
    type = "POST";
  }

  // If we have elements to modify, make the request
  if ( self.length > 0 ) {
    jQuery.ajax( {
      url: url,

      // If "type" variable is undefined, then "GET" method will be used.
      // Make value of this field explicit since
      // user can override it through ajaxSetup method
      type: type || "GET",
      dataType: "html",
      data: params
    } ).done( function( responseText ) {

      // Save response for use in complete callback
      response = arguments;

      self.html( selector ?

        // If a selector was specified, locate the right elements in a dummy div
        // Exclude scripts to avoid IE 'Permission Denied' errors
        jQuery( "<div>" ).append( jQuery.parseHTML( responseText ) ).find( selector ) :

        // Otherwise use the full result
        responseText );

    // If the request succeeds, this function gets "data", "status", "jqXHR"
    // but they are ignored because response was set above.
    // If it fails, this function gets "jqXHR", "status", "error"
    } ).always( callback && function( jqXHR, status ) {
      self.each( function() {
        callback.apply( this, response || [ jqXHR.responseText, status, jqXHR ] );
      } );
    } );
  }

  return this;
};




// Attach a bunch of functions for handling common AJAX events
jQuery.each( [
  "ajaxStart",
  "ajaxStop",
  "ajaxComplete",
  "ajaxError",
  "ajaxSuccess",
  "ajaxSend"
], function( i, type ) {
  jQuery.fn[ type ] = function( fn ) {
    return this.on( type, fn );
  };
} );




jQuery.expr.filters.animated = function( elem ) {
  return jQuery.grep( jQuery.timers, function( fn ) {
    return elem === fn.elem;
  } ).length;
};




/**
 * Gets a window from an element
 */
function getWindow( elem ) {
  return jQuery.isWindow( elem ) ? elem : elem.nodeType === 9 && elem.defaultView;
}

jQuery.offset = {
  setOffset: function( elem, options, i ) {
    var curPosition, curLeft, curCSSTop, curTop, curOffset, curCSSLeft, calculatePosition,
      position = jQuery.css( elem, "position" ),
      curElem = jQuery( elem ),
      props = {};

    // Set position first, in-case top/left are set even on static elem
    if ( position === "static" ) {
      elem.style.position = "relative";
    }

    curOffset = curElem.offset();
    curCSSTop = jQuery.css( elem, "top" );
    curCSSLeft = jQuery.css( elem, "left" );
    calculatePosition = ( position === "absolute" || position === "fixed" ) &&
      ( curCSSTop + curCSSLeft ).indexOf( "auto" ) > -1;

    // Need to be able to calculate position if either
    // top or left is auto and position is either absolute or fixed
    if ( calculatePosition ) {
      curPosition = curElem.position();
      curTop = curPosition.top;
      curLeft = curPosition.left;

    } else {
      curTop = parseFloat( curCSSTop ) || 0;
      curLeft = parseFloat( curCSSLeft ) || 0;
    }

    if ( jQuery.isFunction( options ) ) {

      // Use jQuery.extend here to allow modification of coordinates argument (gh-1848)
      options = options.call( elem, i, jQuery.extend( {}, curOffset ) );
    }

    if ( options.top != null ) {
      props.top = ( options.top - curOffset.top ) + curTop;
    }
    if ( options.left != null ) {
      props.left = ( options.left - curOffset.left ) + curLeft;
    }

    if ( "using" in options ) {
      options.using.call( elem, props );

    } else {
      curElem.css( props );
    }
  }
};

jQuery.fn.extend( {
  offset: function( options ) {
    if ( arguments.length ) {
      return options === undefined ?
        this :
        this.each( function( i ) {
          jQuery.offset.setOffset( this, options, i );
        } );
    }

    var docElem, win,
      elem = this[ 0 ],
      box = { top: 0, left: 0 },
      doc = elem && elem.ownerDocument;

    if ( !doc ) {
      return;
    }

    docElem = doc.documentElement;

    // Make sure it's not a disconnected DOM node
    if ( !jQuery.contains( docElem, elem ) ) {
      return box;
    }

    box = elem.getBoundingClientRect();
    win = getWindow( doc );
    return {
      top: box.top + win.pageYOffset - docElem.clientTop,
      left: box.left + win.pageXOffset - docElem.clientLeft
    };
  },

  position: function() {
    if ( !this[ 0 ] ) {
      return;
    }

    var offsetParent, offset,
      elem = this[ 0 ],
      parentOffset = { top: 0, left: 0 };

    // Fixed elements are offset from window (parentOffset = {top:0, left: 0},
    // because it is its only offset parent
    if ( jQuery.css( elem, "position" ) === "fixed" ) {

      // Assume getBoundingClientRect is there when computed position is fixed
      offset = elem.getBoundingClientRect();

    } else {

      // Get *real* offsetParent
      offsetParent = this.offsetParent();

      // Get correct offsets
      offset = this.offset();
      if ( !jQuery.nodeName( offsetParent[ 0 ], "html" ) ) {
        parentOffset = offsetParent.offset();
      }

      // Add offsetParent borders
      parentOffset.top += jQuery.css( offsetParent[ 0 ], "borderTopWidth", true );
      parentOffset.left += jQuery.css( offsetParent[ 0 ], "borderLeftWidth", true );
    }

    // Subtract parent offsets and element margins
    return {
      top: offset.top - parentOffset.top - jQuery.css( elem, "marginTop", true ),
      left: offset.left - parentOffset.left - jQuery.css( elem, "marginLeft", true )
    };
  },

  // This method will return documentElement in the following cases:
  // 1) For the element inside the iframe without offsetParent, this method will return
  //    documentElement of the parent window
  // 2) For the hidden or detached element
  // 3) For body or html element, i.e. in case of the html node - it will return itself
  //
  // but those exceptions were never presented as a real life use-cases
  // and might be considered as more preferable results.
  //
  // This logic, however, is not guaranteed and can change at any point in the future
  offsetParent: function() {
    return this.map( function() {
      var offsetParent = this.offsetParent;

      while ( offsetParent && jQuery.css( offsetParent, "position" ) === "static" ) {
        offsetParent = offsetParent.offsetParent;
      }

      return offsetParent || documentElement;
    } );
  }
} );

// Create scrollLeft and scrollTop methods
jQuery.each( { scrollLeft: "pageXOffset", scrollTop: "pageYOffset" }, function( method, prop ) {
  var top = "pageYOffset" === prop;

  jQuery.fn[ method ] = function( val ) {
    return access( this, function( elem, method, val ) {
      var win = getWindow( elem );

      if ( val === undefined ) {
        return win ? win[ prop ] : elem[ method ];
      }

      if ( win ) {
        win.scrollTo(
          !top ? val : win.pageXOffset,
          top ? val : win.pageYOffset
        );

      } else {
        elem[ method ] = val;
      }
    }, method, val, arguments.length );
  };
} );

// Support: Safari<7-8+, Chrome<37-44+
// Add the top/left cssHooks using jQuery.fn.position
// Webkit bug: https://bugs.webkit.org/show_bug.cgi?id=29084
// Blink bug: https://code.google.com/p/chromium/issues/detail?id=229280
// getComputedStyle returns percent when specified for top/left/bottom/right;
// rather than make the css module depend on the offset module, just check for it here
jQuery.each( [ "top", "left" ], function( i, prop ) {
  jQuery.cssHooks[ prop ] = addGetHookIf( support.pixelPosition,
    function( elem, computed ) {
      if ( computed ) {
        computed = curCSS( elem, prop );

        // If curCSS returns percentage, fallback to offset
        return rnumnonpx.test( computed ) ?
          jQuery( elem ).position()[ prop ] + "px" :
          computed;
      }
    }
  );
} );


// Create innerHeight, innerWidth, height, width, outerHeight and outerWidth methods
jQuery.each( { Height: "height", Width: "width" }, function( name, type ) {
  jQuery.each( { padding: "inner" + name, content: type, "": "outer" + name },
    function( defaultExtra, funcName ) {

    // Margin is only for outerHeight, outerWidth
    jQuery.fn[ funcName ] = function( margin, value ) {
      var chainable = arguments.length && ( defaultExtra || typeof margin !== "boolean" ),
        extra = defaultExtra || ( margin === true || value === true ? "margin" : "border" );

      return access( this, function( elem, type, value ) {
        var doc;

        if ( jQuery.isWindow( elem ) ) {

          // As of 5/8/2012 this will yield incorrect results for Mobile Safari, but there
          // isn't a whole lot we can do. See pull request at this URL for discussion:
          // https://github.com/jquery/jquery/pull/764
          return elem.document.documentElement[ "client" + name ];
        }

        // Get document width or height
        if ( elem.nodeType === 9 ) {
          doc = elem.documentElement;

          // Either scroll[Width/Height] or offset[Width/Height] or client[Width/Height],
          // whichever is greatest
          return Math.max(
            elem.body[ "scroll" + name ], doc[ "scroll" + name ],
            elem.body[ "offset" + name ], doc[ "offset" + name ],
            doc[ "client" + name ]
          );
        }

        return value === undefined ?

          // Get width or height on the element, requesting but not forcing parseFloat
          jQuery.css( elem, type, extra ) :

          // Set width or height on the element
          jQuery.style( elem, type, value, extra );
      }, type, chainable ? margin : undefined, chainable, null );
    };
  } );
} );


jQuery.fn.extend( {

  bind: function( types, data, fn ) {
    return this.on( types, null, data, fn );
  },
  unbind: function( types, fn ) {
    return this.off( types, null, fn );
  },

  delegate: function( selector, types, data, fn ) {
    return this.on( types, selector, data, fn );
  },
  undelegate: function( selector, types, fn ) {

    // ( namespace ) or ( selector, types [, fn] )
    return arguments.length === 1 ?
      this.off( selector, "**" ) :
      this.off( types, selector || "**", fn );
  },
  size: function() {
    return this.length;
  }
} );

jQuery.fn.andSelf = jQuery.fn.addBack;




// Register as a named AMD module, since jQuery can be concatenated with other
// files that may use define, but not via a proper concatenation script that
// understands anonymous AMD modules. A named AMD is safest and most robust
// way to register. Lowercase jquery is used because AMD module names are
// derived from file names, and jQuery is normally delivered in a lowercase
// file name. Do this after creating the global so that if an AMD module wants
// to call noConflict to hide this version of jQuery, it will work.

// Note that for maximum portability, libraries that are not jQuery should
// declare themselves as anonymous modules, and avoid setting a global if an
// AMD loader is present. jQuery is a special case. For more information, see
// https://github.com/jrburke/requirejs/wiki/Updating-existing-libraries#wiki-anon

if ( typeof define === "function" && define.amd ) {
  define( "jquery", [], function() {
    return jQuery;
  } );
}



var

  // Map over jQuery in case of overwrite
  _jQuery = window.jQuery,

  // Map over the $ in case of overwrite
  _$ = window.$;

jQuery.noConflict = function( deep ) {
  if ( window.$ === jQuery ) {
    window.$ = _$;
  }

  if ( deep && window.jQuery === jQuery ) {
    window.jQuery = _jQuery;
  }

  return jQuery;
};

// Expose jQuery and $ identifiers, even in AMD
// (#7102#comment:10, https://github.com/jquery/jquery/pull/557)
// and CommonJS for browser emulators (#13566)
if ( !noGlobal ) {
  window.jQuery = window.$ = jQuery;
}

return jQuery;
}));

/*!
 * clipboard.js v1.5.12
 * https://zenorocha.github.io/clipboard.js
 *
 * Licensed MIT © Zeno Rocha
 */
(function(f){if(typeof exports==="object"&&typeof module!=="undefined"){module.exports=f()}else if(typeof define==="function"&&define.amd){define([],f)}else{var g;if(typeof window!=="undefined"){g=window}else if(typeof global!=="undefined"){g=global}else if(typeof self!=="undefined"){g=self}else{g=this}g.Clipboard = f()}})(function(){var define,module,exports;return (function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
var matches = require('matches-selector')

module.exports = function (element, selector, checkYoSelf) {
  var parent = checkYoSelf ? element : element.parentNode

  while (parent && parent !== document) {
    if (matches(parent, selector)) return parent;
    parent = parent.parentNode
  }
}

},{"matches-selector":5}],2:[function(require,module,exports){
var closest = require('closest');

/**
 * Delegates event to a selector.
 *
 * @param {Element} element
 * @param {String} selector
 * @param {String} type
 * @param {Function} callback
 * @param {Boolean} useCapture
 * @return {Object}
 */
function delegate(element, selector, type, callback, useCapture) {
    var listenerFn = listener.apply(this, arguments);

    element.addEventListener(type, listenerFn, useCapture);

    return {
        destroy: function() {
            element.removeEventListener(type, listenerFn, useCapture);
        }
    }
}

/**
 * Finds closest match and invokes callback.
 *
 * @param {Element} element
 * @param {String} selector
 * @param {String} type
 * @param {Function} callback
 * @return {Function}
 */
function listener(element, selector, type, callback) {
    return function(e) {
        e.delegateTarget = closest(e.target, selector, true);

        if (e.delegateTarget) {
            callback.call(element, e);
        }
    }
}

module.exports = delegate;

},{"closest":1}],3:[function(require,module,exports){
/**
 * Check if argument is a HTML element.
 *
 * @param {Object} value
 * @return {Boolean}
 */
exports.node = function(value) {
    return value !== undefined
        && value instanceof HTMLElement
        && value.nodeType === 1;
};

/**
 * Check if argument is a list of HTML elements.
 *
 * @param {Object} value
 * @return {Boolean}
 */
exports.nodeList = function(value) {
    var type = Object.prototype.toString.call(value);

    return value !== undefined
        && (type === '[object NodeList]' || type === '[object HTMLCollection]')
        && ('length' in value)
        && (value.length === 0 || exports.node(value[0]));
};

/**
 * Check if argument is a string.
 *
 * @param {Object} value
 * @return {Boolean}
 */
exports.string = function(value) {
    return typeof value === 'string'
        || value instanceof String;
};

/**
 * Check if argument is a function.
 *
 * @param {Object} value
 * @return {Boolean}
 */
exports.fn = function(value) {
    var type = Object.prototype.toString.call(value);

    return type === '[object Function]';
};

},{}],4:[function(require,module,exports){
var is = require('./is');
var delegate = require('delegate');

/**
 * Validates all params and calls the right
 * listener function based on its target type.
 *
 * @param {String|HTMLElement|HTMLCollection|NodeList} target
 * @param {String} type
 * @param {Function} callback
 * @return {Object}
 */
function listen(target, type, callback) {
    if (!target && !type && !callback) {
        throw new Error('Missing required arguments');
    }

    if (!is.string(type)) {
        throw new TypeError('Second argument must be a String');
    }

    if (!is.fn(callback)) {
        throw new TypeError('Third argument must be a Function');
    }

    if (is.node(target)) {
        return listenNode(target, type, callback);
    }
    else if (is.nodeList(target)) {
        return listenNodeList(target, type, callback);
    }
    else if (is.string(target)) {
        return listenSelector(target, type, callback);
    }
    else {
        throw new TypeError('First argument must be a String, HTMLElement, HTMLCollection, or NodeList');
    }
}

/**
 * Adds an event listener to a HTML element
 * and returns a remove listener function.
 *
 * @param {HTMLElement} node
 * @param {String} type
 * @param {Function} callback
 * @return {Object}
 */
function listenNode(node, type, callback) {
    node.addEventListener(type, callback);

    return {
        destroy: function() {
            node.removeEventListener(type, callback);
        }
    }
}

/**
 * Add an event listener to a list of HTML elements
 * and returns a remove listener function.
 *
 * @param {NodeList|HTMLCollection} nodeList
 * @param {String} type
 * @param {Function} callback
 * @return {Object}
 */
function listenNodeList(nodeList, type, callback) {
    Array.prototype.forEach.call(nodeList, function(node) {
        node.addEventListener(type, callback);
    });

    return {
        destroy: function() {
            Array.prototype.forEach.call(nodeList, function(node) {
                node.removeEventListener(type, callback);
            });
        }
    }
}

/**
 * Add an event listener to a selector
 * and returns a remove listener function.
 *
 * @param {String} selector
 * @param {String} type
 * @param {Function} callback
 * @return {Object}
 */
function listenSelector(selector, type, callback) {
    return delegate(document.body, selector, type, callback);
}

module.exports = listen;

},{"./is":3,"delegate":2}],5:[function(require,module,exports){

/**
 * Element prototype.
 */

var proto = Element.prototype;

/**
 * Vendor function.
 */

var vendor = proto.matchesSelector
  || proto.webkitMatchesSelector
  || proto.mozMatchesSelector
  || proto.msMatchesSelector
  || proto.oMatchesSelector;

/**
 * Expose `match()`.
 */

module.exports = match;

/**
 * Match `el` to `selector`.
 *
 * @param {Element} el
 * @param {String} selector
 * @return {Boolean}
 * @api public
 */

function match(el, selector) {
  if (vendor) return vendor.call(el, selector);
  var nodes = el.parentNode.querySelectorAll(selector);
  for (var i = 0; i < nodes.length; ++i) {
    if (nodes[i] == el) return true;
  }
  return false;
}
},{}],6:[function(require,module,exports){
function select(element) {
    var selectedText;

    if (element.nodeName === 'INPUT' || element.nodeName === 'TEXTAREA') {
        element.focus();
        element.setSelectionRange(0, element.value.length);

        selectedText = element.value;
    }
    else {
        if (element.hasAttribute('contenteditable')) {
            element.focus();
        }

        var selection = window.getSelection();
        var range = document.createRange();

        range.selectNodeContents(element);
        selection.removeAllRanges();
        selection.addRange(range);

        selectedText = selection.toString();
    }

    return selectedText;
}

module.exports = select;

},{}],7:[function(require,module,exports){
function E () {
  // Keep this empty so it's easier to inherit from
  // (via https://github.com/lipsmack from https://github.com/scottcorgan/tiny-emitter/issues/3)
}

E.prototype = {
  on: function (name, callback, ctx) {
    var e = this.e || (this.e = {});

    (e[name] || (e[name] = [])).push({
      fn: callback,
      ctx: ctx
    });

    return this;
  },

  once: function (name, callback, ctx) {
    var self = this;
    function listener () {
      self.off(name, listener);
      callback.apply(ctx, arguments);
    };

    listener._ = callback
    return this.on(name, listener, ctx);
  },

  emit: function (name) {
    var data = [].slice.call(arguments, 1);
    var evtArr = ((this.e || (this.e = {}))[name] || []).slice();
    var i = 0;
    var len = evtArr.length;

    for (i; i < len; i++) {
      evtArr[i].fn.apply(evtArr[i].ctx, data);
    }

    return this;
  },

  off: function (name, callback) {
    var e = this.e || (this.e = {});
    var evts = e[name];
    var liveEvents = [];

    if (evts && callback) {
      for (var i = 0, len = evts.length; i < len; i++) {
        if (evts[i].fn !== callback && evts[i].fn._ !== callback)
          liveEvents.push(evts[i]);
      }
    }

    // Remove event from queue to prevent memory leak
    // Suggested by https://github.com/lazd
    // Ref: https://github.com/scottcorgan/tiny-emitter/commit/c6ebfaa9bc973b33d110a84a307742b7cf94c953#commitcomment-5024910

    (liveEvents.length)
      ? e[name] = liveEvents
      : delete e[name];

    return this;
  }
};

module.exports = E;

},{}],8:[function(require,module,exports){
(function (global, factory) {
    if (typeof define === "function" && define.amd) {
        define(['module', 'select'], factory);
    } else if (typeof exports !== "undefined") {
        factory(module, require('select'));
    } else {
        var mod = {
            exports: {}
        };
        factory(mod, global.select);
        global.clipboardAction = mod.exports;
    }
})(this, function (module, _select) {
    'use strict';

    var _select2 = _interopRequireDefault(_select);

    function _interopRequireDefault(obj) {
        return obj && obj.__esModule ? obj : {
            default: obj
        };
    }

    var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
        return typeof obj;
    } : function (obj) {
        return obj && typeof Symbol === "function" && obj.constructor === Symbol ? "symbol" : typeof obj;
    };

    function _classCallCheck(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
            throw new TypeError("Cannot call a class as a function");
        }
    }

    var _createClass = function () {
        function defineProperties(target, props) {
            for (var i = 0; i < props.length; i++) {
                var descriptor = props[i];
                descriptor.enumerable = descriptor.enumerable || false;
                descriptor.configurable = true;
                if ("value" in descriptor) descriptor.writable = true;
                Object.defineProperty(target, descriptor.key, descriptor);
            }
        }

        return function (Constructor, protoProps, staticProps) {
            if (protoProps) defineProperties(Constructor.prototype, protoProps);
            if (staticProps) defineProperties(Constructor, staticProps);
            return Constructor;
        };
    }();

    var ClipboardAction = function () {
        /**
         * @param {Object} options
         */

        function ClipboardAction(options) {
            _classCallCheck(this, ClipboardAction);

            this.resolveOptions(options);
            this.initSelection();
        }

        /**
         * Defines base properties passed from constructor.
         * @param {Object} options
         */


        ClipboardAction.prototype.resolveOptions = function resolveOptions() {
            var options = arguments.length <= 0 || arguments[0] === undefined ? {} : arguments[0];

            this.action = options.action;
            this.emitter = options.emitter;
            this.target = options.target;
            this.text = options.text;
            this.trigger = options.trigger;

            this.selectedText = '';
        };

        ClipboardAction.prototype.initSelection = function initSelection() {
            if (this.text) {
                this.selectFake();
            } else if (this.target) {
                this.selectTarget();
            }
        };

        ClipboardAction.prototype.selectFake = function selectFake() {
            var _this = this;

            var isRTL = document.documentElement.getAttribute('dir') == 'rtl';

            this.removeFake();

            this.fakeHandlerCallback = function () {
                return _this.removeFake();
            };
            this.fakeHandler = document.body.addEventListener('click', this.fakeHandlerCallback) || true;

            this.fakeElem = document.createElement('textarea');
            // Prevent zooming on iOS
            this.fakeElem.style.fontSize = '12pt';
            // Reset box model
            this.fakeElem.style.border = '0';
            this.fakeElem.style.padding = '0';
            this.fakeElem.style.margin = '0';
            // Move element out of screen horizontally
            this.fakeElem.style.position = 'absolute';
            this.fakeElem.style[isRTL ? 'right' : 'left'] = '-9999px';
            // Move element to the same position vertically
            this.fakeElem.style.top = (window.pageYOffset || document.documentElement.scrollTop) + 'px';
            this.fakeElem.setAttribute('readonly', '');
            this.fakeElem.value = this.text;

            document.body.appendChild(this.fakeElem);

            this.selectedText = (0, _select2.default)(this.fakeElem);
            this.copyText();
        };

        ClipboardAction.prototype.removeFake = function removeFake() {
            if (this.fakeHandler) {
                document.body.removeEventListener('click', this.fakeHandlerCallback);
                this.fakeHandler = null;
                this.fakeHandlerCallback = null;
            }

            if (this.fakeElem) {
                document.body.removeChild(this.fakeElem);
                this.fakeElem = null;
            }
        };

        ClipboardAction.prototype.selectTarget = function selectTarget() {
            this.selectedText = (0, _select2.default)(this.target);
            this.copyText();
        };

        ClipboardAction.prototype.copyText = function copyText() {
            var succeeded = undefined;

            try {
                succeeded = document.execCommand(this.action);
            } catch (err) {
                succeeded = false;
            }

            this.handleResult(succeeded);
        };

        ClipboardAction.prototype.handleResult = function handleResult(succeeded) {
            if (succeeded) {
                this.emitter.emit('success', {
                    action: this.action,
                    text: this.selectedText,
                    trigger: this.trigger,
                    clearSelection: this.clearSelection.bind(this)
                });
            } else {
                this.emitter.emit('error', {
                    action: this.action,
                    trigger: this.trigger,
                    clearSelection: this.clearSelection.bind(this)
                });
            }
        };

        ClipboardAction.prototype.clearSelection = function clearSelection() {
            if (this.target) {
                this.target.blur();
            }

            window.getSelection().removeAllRanges();
        };

        ClipboardAction.prototype.destroy = function destroy() {
            this.removeFake();
        };

        _createClass(ClipboardAction, [{
            key: 'action',
            set: function set() {
                var action = arguments.length <= 0 || arguments[0] === undefined ? 'copy' : arguments[0];

                this._action = action;

                if (this._action !== 'copy' && this._action !== 'cut') {
                    throw new Error('Invalid "action" value, use either "copy" or "cut"');
                }
            },
            get: function get() {
                return this._action;
            }
        }, {
            key: 'target',
            set: function set(target) {
                if (target !== undefined) {
                    if (target && (typeof target === 'undefined' ? 'undefined' : _typeof(target)) === 'object' && target.nodeType === 1) {
                        if (this.action === 'copy' && target.hasAttribute('disabled')) {
                            throw new Error('Invalid "target" attribute. Please use "readonly" instead of "disabled" attribute');
                        }

                        if (this.action === 'cut' && (target.hasAttribute('readonly') || target.hasAttribute('disabled'))) {
                            throw new Error('Invalid "target" attribute. You can\'t cut text from elements with "readonly" or "disabled" attributes');
                        }

                        this._target = target;
                    } else {
                        throw new Error('Invalid "target" value, use a valid Element');
                    }
                }
            },
            get: function get() {
                return this._target;
            }
        }]);

        return ClipboardAction;
    }();

    module.exports = ClipboardAction;
});

},{"select":6}],9:[function(require,module,exports){
(function (global, factory) {
    if (typeof define === "function" && define.amd) {
        define(['module', './clipboard-action', 'tiny-emitter', 'good-listener'], factory);
    } else if (typeof exports !== "undefined") {
        factory(module, require('./clipboard-action'), require('tiny-emitter'), require('good-listener'));
    } else {
        var mod = {
            exports: {}
        };
        factory(mod, global.clipboardAction, global.tinyEmitter, global.goodListener);
        global.clipboard = mod.exports;
    }
})(this, function (module, _clipboardAction, _tinyEmitter, _goodListener) {
    'use strict';

    var _clipboardAction2 = _interopRequireDefault(_clipboardAction);

    var _tinyEmitter2 = _interopRequireDefault(_tinyEmitter);

    var _goodListener2 = _interopRequireDefault(_goodListener);

    function _interopRequireDefault(obj) {
        return obj && obj.__esModule ? obj : {
            default: obj
        };
    }

    function _classCallCheck(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
            throw new TypeError("Cannot call a class as a function");
        }
    }

    function _possibleConstructorReturn(self, call) {
        if (!self) {
            throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
        }

        return call && (typeof call === "object" || typeof call === "function") ? call : self;
    }

    function _inherits(subClass, superClass) {
        if (typeof superClass !== "function" && superClass !== null) {
            throw new TypeError("Super expression must either be null or a function, not " + typeof superClass);
        }

        subClass.prototype = Object.create(superClass && superClass.prototype, {
            constructor: {
                value: subClass,
                enumerable: false,
                writable: true,
                configurable: true
            }
        });
        if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass;
    }

    var Clipboard = function (_Emitter) {
        _inherits(Clipboard, _Emitter);

        /**
         * @param {String|HTMLElement|HTMLCollection|NodeList} trigger
         * @param {Object} options
         */

        function Clipboard(trigger, options) {
            _classCallCheck(this, Clipboard);

            var _this = _possibleConstructorReturn(this, _Emitter.call(this));

            _this.resolveOptions(options);
            _this.listenClick(trigger);
            return _this;
        }

        /**
         * Defines if attributes would be resolved using internal setter functions
         * or custom functions that were passed in the constructor.
         * @param {Object} options
         */


        Clipboard.prototype.resolveOptions = function resolveOptions() {
            var options = arguments.length <= 0 || arguments[0] === undefined ? {} : arguments[0];

            this.action = typeof options.action === 'function' ? options.action : this.defaultAction;
            this.target = typeof options.target === 'function' ? options.target : this.defaultTarget;
            this.text = typeof options.text === 'function' ? options.text : this.defaultText;
        };

        Clipboard.prototype.listenClick = function listenClick(trigger) {
            var _this2 = this;

            this.listener = (0, _goodListener2.default)(trigger, 'click', function (e) {
                return _this2.onClick(e);
            });
        };

        Clipboard.prototype.onClick = function onClick(e) {
            var trigger = e.delegateTarget || e.currentTarget;

            if (this.clipboardAction) {
                this.clipboardAction = null;
            }

            this.clipboardAction = new _clipboardAction2.default({
                action: this.action(trigger),
                target: this.target(trigger),
                text: this.text(trigger),
                trigger: trigger,
                emitter: this
            });
        };

        Clipboard.prototype.defaultAction = function defaultAction(trigger) {
            return getAttributeValue('action', trigger);
        };

        Clipboard.prototype.defaultTarget = function defaultTarget(trigger) {
            var selector = getAttributeValue('target', trigger);

            if (selector) {
                return document.querySelector(selector);
            }
        };

        Clipboard.prototype.defaultText = function defaultText(trigger) {
            return getAttributeValue('text', trigger);
        };

        Clipboard.prototype.destroy = function destroy() {
            this.listener.destroy();

            if (this.clipboardAction) {
                this.clipboardAction.destroy();
                this.clipboardAction = null;
            }
        };

        return Clipboard;
    }(_tinyEmitter2.default);

    /**
     * Helper function to retrieve attribute value.
     * @param {String} suffix
     * @param {Element} element
     */
    function getAttributeValue(suffix, element) {
        var attribute = 'data-clipboard-' + suffix;

        if (!element.hasAttribute(attribute)) {
            return;
        }

        return element.getAttribute(attribute);
    }

    module.exports = Clipboard;
});

},{"./clipboard-action":8,"good-listener":4,"tiny-emitter":7}]},{},[9])(9)
});
/*!
 * Chart.js
 * http://chartjs.org/
 * Version: 2.2.0-rc.2
 *
 * Copyright 2016 Nick Downie
 * Released under the MIT license
 * https://github.com/chartjs/Chart.js/blob/master/LICENSE.md
 */
(function(f){if(typeof exports==="object"&&typeof module!=="undefined"){module.exports=f()}else if(typeof define==="function"&&define.amd){define([],f)}else{var g;if(typeof window!=="undefined"){g=window}else if(typeof global!=="undefined"){g=global}else if(typeof self!=="undefined"){g=self}else{g=this}g.Chart = f()}})(function(){var define,module,exports;return (function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){

},{}],2:[function(require,module,exports){
/* MIT license */
var colorNames = require(6);

module.exports = {
   getRgba: getRgba,
   getHsla: getHsla,
   getRgb: getRgb,
   getHsl: getHsl,
   getHwb: getHwb,
   getAlpha: getAlpha,

   hexString: hexString,
   rgbString: rgbString,
   rgbaString: rgbaString,
   percentString: percentString,
   percentaString: percentaString,
   hslString: hslString,
   hslaString: hslaString,
   hwbString: hwbString,
   keyword: keyword
}

function getRgba(string) {
   if (!string) {
      return;
   }
   var abbr =  /^#([a-fA-F0-9]{3})$/,
       hex =  /^#([a-fA-F0-9]{6})$/,
       rgba = /^rgba?\(\s*([+-]?\d+)\s*,\s*([+-]?\d+)\s*,\s*([+-]?\d+)\s*(?:,\s*([+-]?[\d\.]+)\s*)?\)$/,
       per = /^rgba?\(\s*([+-]?[\d\.]+)\%\s*,\s*([+-]?[\d\.]+)\%\s*,\s*([+-]?[\d\.]+)\%\s*(?:,\s*([+-]?[\d\.]+)\s*)?\)$/,
       keyword = /(\w+)/;

   var rgb = [0, 0, 0],
       a = 1,
       match = string.match(abbr);
   if (match) {
      match = match[1];
      for (var i = 0; i < rgb.length; i++) {
         rgb[i] = parseInt(match[i] + match[i], 16);
      }
   }
   else if (match = string.match(hex)) {
      match = match[1];
      for (var i = 0; i < rgb.length; i++) {
         rgb[i] = parseInt(match.slice(i * 2, i * 2 + 2), 16);
      }
   }
   else if (match = string.match(rgba)) {
      for (var i = 0; i < rgb.length; i++) {
         rgb[i] = parseInt(match[i + 1]);
      }
      a = parseFloat(match[4]);
   }
   else if (match = string.match(per)) {
      for (var i = 0; i < rgb.length; i++) {
         rgb[i] = Math.round(parseFloat(match[i + 1]) * 2.55);
      }
      a = parseFloat(match[4]);
   }
   else if (match = string.match(keyword)) {
      if (match[1] == "transparent") {
         return [0, 0, 0, 0];
      }
      rgb = colorNames[match[1]];
      if (!rgb) {
         return;
      }
   }

   for (var i = 0; i < rgb.length; i++) {
      rgb[i] = scale(rgb[i], 0, 255);
   }
   if (!a && a != 0) {
      a = 1;
   }
   else {
      a = scale(a, 0, 1);
   }
   rgb[3] = a;
   return rgb;
}

function getHsla(string) {
   if (!string) {
      return;
   }
   var hsl = /^hsla?\(\s*([+-]?\d+)(?:deg)?\s*,\s*([+-]?[\d\.]+)%\s*,\s*([+-]?[\d\.]+)%\s*(?:,\s*([+-]?[\d\.]+)\s*)?\)/;
   var match = string.match(hsl);
   if (match) {
      var alpha = parseFloat(match[4]);
      var h = scale(parseInt(match[1]), 0, 360),
          s = scale(parseFloat(match[2]), 0, 100),
          l = scale(parseFloat(match[3]), 0, 100),
          a = scale(isNaN(alpha) ? 1 : alpha, 0, 1);
      return [h, s, l, a];
   }
}

function getHwb(string) {
   if (!string) {
      return;
   }
   var hwb = /^hwb\(\s*([+-]?\d+)(?:deg)?\s*,\s*([+-]?[\d\.]+)%\s*,\s*([+-]?[\d\.]+)%\s*(?:,\s*([+-]?[\d\.]+)\s*)?\)/;
   var match = string.match(hwb);
   if (match) {
    var alpha = parseFloat(match[4]);
      var h = scale(parseInt(match[1]), 0, 360),
          w = scale(parseFloat(match[2]), 0, 100),
          b = scale(parseFloat(match[3]), 0, 100),
          a = scale(isNaN(alpha) ? 1 : alpha, 0, 1);
      return [h, w, b, a];
   }
}

function getRgb(string) {
   var rgba = getRgba(string);
   return rgba && rgba.slice(0, 3);
}

function getHsl(string) {
  var hsla = getHsla(string);
  return hsla && hsla.slice(0, 3);
}

function getAlpha(string) {
   var vals = getRgba(string);
   if (vals) {
      return vals[3];
   }
   else if (vals = getHsla(string)) {
      return vals[3];
   }
   else if (vals = getHwb(string)) {
      return vals[3];
   }
}

// generators
function hexString(rgb) {
   return "#" + hexDouble(rgb[0]) + hexDouble(rgb[1])
              + hexDouble(rgb[2]);
}

function rgbString(rgba, alpha) {
   if (alpha < 1 || (rgba[3] && rgba[3] < 1)) {
      return rgbaString(rgba, alpha);
   }
   return "rgb(" + rgba[0] + ", " + rgba[1] + ", " + rgba[2] + ")";
}

function rgbaString(rgba, alpha) {
   if (alpha === undefined) {
      alpha = (rgba[3] !== undefined ? rgba[3] : 1);
   }
   return "rgba(" + rgba[0] + ", " + rgba[1] + ", " + rgba[2]
           + ", " + alpha + ")";
}

function percentString(rgba, alpha) {
   if (alpha < 1 || (rgba[3] && rgba[3] < 1)) {
      return percentaString(rgba, alpha);
   }
   var r = Math.round(rgba[0]/255 * 100),
       g = Math.round(rgba[1]/255 * 100),
       b = Math.round(rgba[2]/255 * 100);

   return "rgb(" + r + "%, " + g + "%, " + b + "%)";
}

function percentaString(rgba, alpha) {
   var r = Math.round(rgba[0]/255 * 100),
       g = Math.round(rgba[1]/255 * 100),
       b = Math.round(rgba[2]/255 * 100);
   return "rgba(" + r + "%, " + g + "%, " + b + "%, " + (alpha || rgba[3] || 1) + ")";
}

function hslString(hsla, alpha) {
   if (alpha < 1 || (hsla[3] && hsla[3] < 1)) {
      return hslaString(hsla, alpha);
   }
   return "hsl(" + hsla[0] + ", " + hsla[1] + "%, " + hsla[2] + "%)";
}

function hslaString(hsla, alpha) {
   if (alpha === undefined) {
      alpha = (hsla[3] !== undefined ? hsla[3] : 1);
   }
   return "hsla(" + hsla[0] + ", " + hsla[1] + "%, " + hsla[2] + "%, "
           + alpha + ")";
}

// hwb is a bit different than rgb(a) & hsl(a) since there is no alpha specific syntax
// (hwb have alpha optional & 1 is default value)
function hwbString(hwb, alpha) {
   if (alpha === undefined) {
      alpha = (hwb[3] !== undefined ? hwb[3] : 1);
   }
   return "hwb(" + hwb[0] + ", " + hwb[1] + "%, " + hwb[2] + "%"
           + (alpha !== undefined && alpha !== 1 ? ", " + alpha : "") + ")";
}

function keyword(rgb) {
  return reverseNames[rgb.slice(0, 3)];
}

// helpers
function scale(num, min, max) {
   return Math.min(Math.max(min, num), max);
}

function hexDouble(num) {
  var str = num.toString(16).toUpperCase();
  return (str.length < 2) ? "0" + str : str;
}


//create a list of reverse color names
var reverseNames = {};
for (var name in colorNames) {
   reverseNames[colorNames[name]] = name;
}

},{"6":6}],3:[function(require,module,exports){
/* MIT license */
var convert = require(5);
var string = require(2);

var Color = function (obj) {
  if (obj instanceof Color) {
    return obj;
  }
  if (!(this instanceof Color)) {
    return new Color(obj);
  }

  this.values = {
    rgb: [0, 0, 0],
    hsl: [0, 0, 0],
    hsv: [0, 0, 0],
    hwb: [0, 0, 0],
    cmyk: [0, 0, 0, 0],
    alpha: 1
  };

  // parse Color() argument
  var vals;
  if (typeof obj === 'string') {
    vals = string.getRgba(obj);
    if (vals) {
      this.setValues('rgb', vals);
    } else if (vals = string.getHsla(obj)) {
      this.setValues('hsl', vals);
    } else if (vals = string.getHwb(obj)) {
      this.setValues('hwb', vals);
    } else {
      throw new Error('Unable to parse color from string "' + obj + '"');
    }
  } else if (typeof obj === 'object') {
    vals = obj;
    if (vals.r !== undefined || vals.red !== undefined) {
      this.setValues('rgb', vals);
    } else if (vals.l !== undefined || vals.lightness !== undefined) {
      this.setValues('hsl', vals);
    } else if (vals.v !== undefined || vals.value !== undefined) {
      this.setValues('hsv', vals);
    } else if (vals.w !== undefined || vals.whiteness !== undefined) {
      this.setValues('hwb', vals);
    } else if (vals.c !== undefined || vals.cyan !== undefined) {
      this.setValues('cmyk', vals);
    } else {
      throw new Error('Unable to parse color from object ' + JSON.stringify(obj));
    }
  }
};

Color.prototype = {
  rgb: function () {
    return this.setSpace('rgb', arguments);
  },
  hsl: function () {
    return this.setSpace('hsl', arguments);
  },
  hsv: function () {
    return this.setSpace('hsv', arguments);
  },
  hwb: function () {
    return this.setSpace('hwb', arguments);
  },
  cmyk: function () {
    return this.setSpace('cmyk', arguments);
  },

  rgbArray: function () {
    return this.values.rgb;
  },
  hslArray: function () {
    return this.values.hsl;
  },
  hsvArray: function () {
    return this.values.hsv;
  },
  hwbArray: function () {
    var values = this.values;
    if (values.alpha !== 1) {
      return values.hwb.concat([values.alpha]);
    }
    return values.hwb;
  },
  cmykArray: function () {
    return this.values.cmyk;
  },
  rgbaArray: function () {
    var values = this.values;
    return values.rgb.concat([values.alpha]);
  },
  hslaArray: function () {
    var values = this.values;
    return values.hsl.concat([values.alpha]);
  },
  alpha: function (val) {
    if (val === undefined) {
      return this.values.alpha;
    }
    this.setValues('alpha', val);
    return this;
  },

  red: function (val) {
    return this.setChannel('rgb', 0, val);
  },
  green: function (val) {
    return this.setChannel('rgb', 1, val);
  },
  blue: function (val) {
    return this.setChannel('rgb', 2, val);
  },
  hue: function (val) {
    if (val) {
      val %= 360;
      val = val < 0 ? 360 + val : val;
    }
    return this.setChannel('hsl', 0, val);
  },
  saturation: function (val) {
    return this.setChannel('hsl', 1, val);
  },
  lightness: function (val) {
    return this.setChannel('hsl', 2, val);
  },
  saturationv: function (val) {
    return this.setChannel('hsv', 1, val);
  },
  whiteness: function (val) {
    return this.setChannel('hwb', 1, val);
  },
  blackness: function (val) {
    return this.setChannel('hwb', 2, val);
  },
  value: function (val) {
    return this.setChannel('hsv', 2, val);
  },
  cyan: function (val) {
    return this.setChannel('cmyk', 0, val);
  },
  magenta: function (val) {
    return this.setChannel('cmyk', 1, val);
  },
  yellow: function (val) {
    return this.setChannel('cmyk', 2, val);
  },
  black: function (val) {
    return this.setChannel('cmyk', 3, val);
  },

  hexString: function () {
    return string.hexString(this.values.rgb);
  },
  rgbString: function () {
    return string.rgbString(this.values.rgb, this.values.alpha);
  },
  rgbaString: function () {
    return string.rgbaString(this.values.rgb, this.values.alpha);
  },
  percentString: function () {
    return string.percentString(this.values.rgb, this.values.alpha);
  },
  hslString: function () {
    return string.hslString(this.values.hsl, this.values.alpha);
  },
  hslaString: function () {
    return string.hslaString(this.values.hsl, this.values.alpha);
  },
  hwbString: function () {
    return string.hwbString(this.values.hwb, this.values.alpha);
  },
  keyword: function () {
    return string.keyword(this.values.rgb, this.values.alpha);
  },

  rgbNumber: function () {
    var rgb = this.values.rgb;
    return (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
  },

  luminosity: function () {
    // http://www.w3.org/TR/WCAG20/#relativeluminancedef
    var rgb = this.values.rgb;
    var lum = [];
    for (var i = 0; i < rgb.length; i++) {
      var chan = rgb[i] / 255;
      lum[i] = (chan <= 0.03928) ? chan / 12.92 : Math.pow(((chan + 0.055) / 1.055), 2.4);
    }
    return 0.2126 * lum[0] + 0.7152 * lum[1] + 0.0722 * lum[2];
  },

  contrast: function (color2) {
    // http://www.w3.org/TR/WCAG20/#contrast-ratiodef
    var lum1 = this.luminosity();
    var lum2 = color2.luminosity();
    if (lum1 > lum2) {
      return (lum1 + 0.05) / (lum2 + 0.05);
    }
    return (lum2 + 0.05) / (lum1 + 0.05);
  },

  level: function (color2) {
    var contrastRatio = this.contrast(color2);
    if (contrastRatio >= 7.1) {
      return 'AAA';
    }

    return (contrastRatio >= 4.5) ? 'AA' : '';
  },

  dark: function () {
    // YIQ equation from http://24ways.org/2010/calculating-color-contrast
    var rgb = this.values.rgb;
    var yiq = (rgb[0] * 299 + rgb[1] * 587 + rgb[2] * 114) / 1000;
    return yiq < 128;
  },

  light: function () {
    return !this.dark();
  },

  negate: function () {
    var rgb = [];
    for (var i = 0; i < 3; i++) {
      rgb[i] = 255 - this.values.rgb[i];
    }
    this.setValues('rgb', rgb);
    return this;
  },

  lighten: function (ratio) {
    var hsl = this.values.hsl;
    hsl[2] += hsl[2] * ratio;
    this.setValues('hsl', hsl);
    return this;
  },

  darken: function (ratio) {
    var hsl = this.values.hsl;
    hsl[2] -= hsl[2] * ratio;
    this.setValues('hsl', hsl);
    return this;
  },

  saturate: function (ratio) {
    var hsl = this.values.hsl;
    hsl[1] += hsl[1] * ratio;
    this.setValues('hsl', hsl);
    return this;
  },

  desaturate: function (ratio) {
    var hsl = this.values.hsl;
    hsl[1] -= hsl[1] * ratio;
    this.setValues('hsl', hsl);
    return this;
  },

  whiten: function (ratio) {
    var hwb = this.values.hwb;
    hwb[1] += hwb[1] * ratio;
    this.setValues('hwb', hwb);
    return this;
  },

  blacken: function (ratio) {
    var hwb = this.values.hwb;
    hwb[2] += hwb[2] * ratio;
    this.setValues('hwb', hwb);
    return this;
  },

  greyscale: function () {
    var rgb = this.values.rgb;
    // http://en.wikipedia.org/wiki/Grayscale#Converting_color_to_grayscale
    var val = rgb[0] * 0.3 + rgb[1] * 0.59 + rgb[2] * 0.11;
    this.setValues('rgb', [val, val, val]);
    return this;
  },

  clearer: function (ratio) {
    var alpha = this.values.alpha;
    this.setValues('alpha', alpha - (alpha * ratio));
    return this;
  },

  opaquer: function (ratio) {
    var alpha = this.values.alpha;
    this.setValues('alpha', alpha + (alpha * ratio));
    return this;
  },

  rotate: function (degrees) {
    var hsl = this.values.hsl;
    var hue = (hsl[0] + degrees) % 360;
    hsl[0] = hue < 0 ? 360 + hue : hue;
    this.setValues('hsl', hsl);
    return this;
  },

  /**
   * Ported from sass implementation in C
   * https://github.com/sass/libsass/blob/0e6b4a2850092356aa3ece07c6b249f0221caced/functions.cpp#L209
   */
  mix: function (mixinColor, weight) {
    var color1 = this;
    var color2 = mixinColor;
    var p = weight === undefined ? 0.5 : weight;

    var w = 2 * p - 1;
    var a = color1.alpha() - color2.alpha();

    var w1 = (((w * a === -1) ? w : (w + a) / (1 + w * a)) + 1) / 2.0;
    var w2 = 1 - w1;

    return this
      .rgb(
        w1 * color1.red() + w2 * color2.red(),
        w1 * color1.green() + w2 * color2.green(),
        w1 * color1.blue() + w2 * color2.blue()
      )
      .alpha(color1.alpha() * p + color2.alpha() * (1 - p));
  },

  toJSON: function () {
    return this.rgb();
  },

  clone: function () {
    // NOTE(SB): using node-clone creates a dependency to Buffer when using browserify,
    // making the final build way to big to embed in Chart.js. So let's do it manually,
    // assuming that values to clone are 1 dimension arrays containing only numbers,
    // except 'alpha' which is a number.
    var result = new Color();
    var source = this.values;
    var target = result.values;
    var value, type;

    for (var prop in source) {
      if (source.hasOwnProperty(prop)) {
        value = source[prop];
        type = ({}).toString.call(value);
        if (type === '[object Array]') {
          target[prop] = value.slice(0);
        } else if (type === '[object Number]') {
          target[prop] = value;
        } else {
          console.error('unexpected color value:', value);
        }
      }
    }

    return result;
  }
};

Color.prototype.spaces = {
  rgb: ['red', 'green', 'blue'],
  hsl: ['hue', 'saturation', 'lightness'],
  hsv: ['hue', 'saturation', 'value'],
  hwb: ['hue', 'whiteness', 'blackness'],
  cmyk: ['cyan', 'magenta', 'yellow', 'black']
};

Color.prototype.maxes = {
  rgb: [255, 255, 255],
  hsl: [360, 100, 100],
  hsv: [360, 100, 100],
  hwb: [360, 100, 100],
  cmyk: [100, 100, 100, 100]
};

Color.prototype.getValues = function (space) {
  var values = this.values;
  var vals = {};

  for (var i = 0; i < space.length; i++) {
    vals[space.charAt(i)] = values[space][i];
  }

  if (values.alpha !== 1) {
    vals.a = values.alpha;
  }

  // {r: 255, g: 255, b: 255, a: 0.4}
  return vals;
};

Color.prototype.setValues = function (space, vals) {
  var values = this.values;
  var spaces = this.spaces;
  var maxes = this.maxes;
  var alpha = 1;
  var i;

  if (space === 'alpha') {
    alpha = vals;
  } else if (vals.length) {
    // [10, 10, 10]
    values[space] = vals.slice(0, space.length);
    alpha = vals[space.length];
  } else if (vals[space.charAt(0)] !== undefined) {
    // {r: 10, g: 10, b: 10}
    for (i = 0; i < space.length; i++) {
      values[space][i] = vals[space.charAt(i)];
    }

    alpha = vals.a;
  } else if (vals[spaces[space][0]] !== undefined) {
    // {red: 10, green: 10, blue: 10}
    var chans = spaces[space];

    for (i = 0; i < space.length; i++) {
      values[space][i] = vals[chans[i]];
    }

    alpha = vals.alpha;
  }

  values.alpha = Math.max(0, Math.min(1, (alpha === undefined ? values.alpha : alpha)));

  if (space === 'alpha') {
    return false;
  }

  var capped;

  // cap values of the space prior converting all values
  for (i = 0; i < space.length; i++) {
    capped = Math.max(0, Math.min(maxes[space][i], values[space][i]));
    values[space][i] = Math.round(capped);
  }

  // convert to all the other color spaces
  for (var sname in spaces) {
    if (sname !== space) {
      values[sname] = convert[space][sname](values[space]);
    }
  }

  return true;
};

Color.prototype.setSpace = function (space, args) {
  var vals = args[0];

  if (vals === undefined) {
    // color.rgb()
    return this.getValues(space);
  }

  // color.rgb(10, 10, 10)
  if (typeof vals === 'number') {
    vals = Array.prototype.slice.call(args);
  }

  this.setValues(space, vals);
  return this;
};

Color.prototype.setChannel = function (space, index, val) {
  var svalues = this.values[space];
  if (val === undefined) {
    // color.red()
    return svalues[index];
  } else if (val === svalues[index]) {
    // color.red(color.red())
    return this;
  }

  // color.red(100)
  svalues[index] = val;
  this.setValues(space, svalues);

  return this;
};

if (typeof window !== 'undefined') {
  window.Color = Color;
}

module.exports = Color;

},{"2":2,"5":5}],4:[function(require,module,exports){
/* MIT license */

module.exports = {
  rgb2hsl: rgb2hsl,
  rgb2hsv: rgb2hsv,
  rgb2hwb: rgb2hwb,
  rgb2cmyk: rgb2cmyk,
  rgb2keyword: rgb2keyword,
  rgb2xyz: rgb2xyz,
  rgb2lab: rgb2lab,
  rgb2lch: rgb2lch,

  hsl2rgb: hsl2rgb,
  hsl2hsv: hsl2hsv,
  hsl2hwb: hsl2hwb,
  hsl2cmyk: hsl2cmyk,
  hsl2keyword: hsl2keyword,

  hsv2rgb: hsv2rgb,
  hsv2hsl: hsv2hsl,
  hsv2hwb: hsv2hwb,
  hsv2cmyk: hsv2cmyk,
  hsv2keyword: hsv2keyword,

  hwb2rgb: hwb2rgb,
  hwb2hsl: hwb2hsl,
  hwb2hsv: hwb2hsv,
  hwb2cmyk: hwb2cmyk,
  hwb2keyword: hwb2keyword,

  cmyk2rgb: cmyk2rgb,
  cmyk2hsl: cmyk2hsl,
  cmyk2hsv: cmyk2hsv,
  cmyk2hwb: cmyk2hwb,
  cmyk2keyword: cmyk2keyword,

  keyword2rgb: keyword2rgb,
  keyword2hsl: keyword2hsl,
  keyword2hsv: keyword2hsv,
  keyword2hwb: keyword2hwb,
  keyword2cmyk: keyword2cmyk,
  keyword2lab: keyword2lab,
  keyword2xyz: keyword2xyz,

  xyz2rgb: xyz2rgb,
  xyz2lab: xyz2lab,
  xyz2lch: xyz2lch,

  lab2xyz: lab2xyz,
  lab2rgb: lab2rgb,
  lab2lch: lab2lch,

  lch2lab: lch2lab,
  lch2xyz: lch2xyz,
  lch2rgb: lch2rgb
}


function rgb2hsl(rgb) {
  var r = rgb[0]/255,
      g = rgb[1]/255,
      b = rgb[2]/255,
      min = Math.min(r, g, b),
      max = Math.max(r, g, b),
      delta = max - min,
      h, s, l;

  if (max == min)
    h = 0;
  else if (r == max)
    h = (g - b) / delta;
  else if (g == max)
    h = 2 + (b - r) / delta;
  else if (b == max)
    h = 4 + (r - g)/ delta;

  h = Math.min(h * 60, 360);

  if (h < 0)
    h += 360;

  l = (min + max) / 2;

  if (max == min)
    s = 0;
  else if (l <= 0.5)
    s = delta / (max + min);
  else
    s = delta / (2 - max - min);

  return [h, s * 100, l * 100];
}

function rgb2hsv(rgb) {
  var r = rgb[0],
      g = rgb[1],
      b = rgb[2],
      min = Math.min(r, g, b),
      max = Math.max(r, g, b),
      delta = max - min,
      h, s, v;

  if (max == 0)
    s = 0;
  else
    s = (delta/max * 1000)/10;

  if (max == min)
    h = 0;
  else if (r == max)
    h = (g - b) / delta;
  else if (g == max)
    h = 2 + (b - r) / delta;
  else if (b == max)
    h = 4 + (r - g) / delta;

  h = Math.min(h * 60, 360);

  if (h < 0)
    h += 360;

  v = ((max / 255) * 1000) / 10;

  return [h, s, v];
}

function rgb2hwb(rgb) {
  var r = rgb[0],
      g = rgb[1],
      b = rgb[2],
      h = rgb2hsl(rgb)[0],
      w = 1/255 * Math.min(r, Math.min(g, b)),
      b = 1 - 1/255 * Math.max(r, Math.max(g, b));

  return [h, w * 100, b * 100];
}

function rgb2cmyk(rgb) {
  var r = rgb[0] / 255,
      g = rgb[1] / 255,
      b = rgb[2] / 255,
      c, m, y, k;

  k = Math.min(1 - r, 1 - g, 1 - b);
  c = (1 - r - k) / (1 - k) || 0;
  m = (1 - g - k) / (1 - k) || 0;
  y = (1 - b - k) / (1 - k) || 0;
  return [c * 100, m * 100, y * 100, k * 100];
}

function rgb2keyword(rgb) {
  return reverseKeywords[JSON.stringify(rgb)];
}

function rgb2xyz(rgb) {
  var r = rgb[0] / 255,
      g = rgb[1] / 255,
      b = rgb[2] / 255;

  // assume sRGB
  r = r > 0.04045 ? Math.pow(((r + 0.055) / 1.055), 2.4) : (r / 12.92);
  g = g > 0.04045 ? Math.pow(((g + 0.055) / 1.055), 2.4) : (g / 12.92);
  b = b > 0.04045 ? Math.pow(((b + 0.055) / 1.055), 2.4) : (b / 12.92);

  var x = (r * 0.4124) + (g * 0.3576) + (b * 0.1805);
  var y = (r * 0.2126) + (g * 0.7152) + (b * 0.0722);
  var z = (r * 0.0193) + (g * 0.1192) + (b * 0.9505);

  return [x * 100, y *100, z * 100];
}

function rgb2lab(rgb) {
  var xyz = rgb2xyz(rgb),
        x = xyz[0],
        y = xyz[1],
        z = xyz[2],
        l, a, b;

  x /= 95.047;
  y /= 100;
  z /= 108.883;

  x = x > 0.008856 ? Math.pow(x, 1/3) : (7.787 * x) + (16 / 116);
  y = y > 0.008856 ? Math.pow(y, 1/3) : (7.787 * y) + (16 / 116);
  z = z > 0.008856 ? Math.pow(z, 1/3) : (7.787 * z) + (16 / 116);

  l = (116 * y) - 16;
  a = 500 * (x - y);
  b = 200 * (y - z);

  return [l, a, b];
}

function rgb2lch(args) {
  return lab2lch(rgb2lab(args));
}

function hsl2rgb(hsl) {
  var h = hsl[0] / 360,
      s = hsl[1] / 100,
      l = hsl[2] / 100,
      t1, t2, t3, rgb, val;

  if (s == 0) {
    val = l * 255;
    return [val, val, val];
  }

  if (l < 0.5)
    t2 = l * (1 + s);
  else
    t2 = l + s - l * s;
  t1 = 2 * l - t2;

  rgb = [0, 0, 0];
  for (var i = 0; i < 3; i++) {
    t3 = h + 1 / 3 * - (i - 1);
    t3 < 0 && t3++;
    t3 > 1 && t3--;

    if (6 * t3 < 1)
      val = t1 + (t2 - t1) * 6 * t3;
    else if (2 * t3 < 1)
      val = t2;
    else if (3 * t3 < 2)
      val = t1 + (t2 - t1) * (2 / 3 - t3) * 6;
    else
      val = t1;

    rgb[i] = val * 255;
  }

  return rgb;
}

function hsl2hsv(hsl) {
  var h = hsl[0],
      s = hsl[1] / 100,
      l = hsl[2] / 100,
      sv, v;

  if(l === 0) {
      // no need to do calc on black
      // also avoids divide by 0 error
      return [0, 0, 0];
  }

  l *= 2;
  s *= (l <= 1) ? l : 2 - l;
  v = (l + s) / 2;
  sv = (2 * s) / (l + s);
  return [h, sv * 100, v * 100];
}

function hsl2hwb(args) {
  return rgb2hwb(hsl2rgb(args));
}

function hsl2cmyk(args) {
  return rgb2cmyk(hsl2rgb(args));
}

function hsl2keyword(args) {
  return rgb2keyword(hsl2rgb(args));
}


function hsv2rgb(hsv) {
  var h = hsv[0] / 60,
      s = hsv[1] / 100,
      v = hsv[2] / 100,
      hi = Math.floor(h) % 6;

  var f = h - Math.floor(h),
      p = 255 * v * (1 - s),
      q = 255 * v * (1 - (s * f)),
      t = 255 * v * (1 - (s * (1 - f))),
      v = 255 * v;

  switch(hi) {
    case 0:
      return [v, t, p];
    case 1:
      return [q, v, p];
    case 2:
      return [p, v, t];
    case 3:
      return [p, q, v];
    case 4:
      return [t, p, v];
    case 5:
      return [v, p, q];
  }
}

function hsv2hsl(hsv) {
  var h = hsv[0],
      s = hsv[1] / 100,
      v = hsv[2] / 100,
      sl, l;

  l = (2 - s) * v;
  sl = s * v;
  sl /= (l <= 1) ? l : 2 - l;
  sl = sl || 0;
  l /= 2;
  return [h, sl * 100, l * 100];
}

function hsv2hwb(args) {
  return rgb2hwb(hsv2rgb(args))
}

function hsv2cmyk(args) {
  return rgb2cmyk(hsv2rgb(args));
}

function hsv2keyword(args) {
  return rgb2keyword(hsv2rgb(args));
}

// http://dev.w3.org/csswg/css-color/#hwb-to-rgb
function hwb2rgb(hwb) {
  var h = hwb[0] / 360,
      wh = hwb[1] / 100,
      bl = hwb[2] / 100,
      ratio = wh + bl,
      i, v, f, n;

  // wh + bl cant be > 1
  if (ratio > 1) {
    wh /= ratio;
    bl /= ratio;
  }

  i = Math.floor(6 * h);
  v = 1 - bl;
  f = 6 * h - i;
  if ((i & 0x01) != 0) {
    f = 1 - f;
  }
  n = wh + f * (v - wh);  // linear interpolation

  switch (i) {
    default:
    case 6:
    case 0: r = v; g = n; b = wh; break;
    case 1: r = n; g = v; b = wh; break;
    case 2: r = wh; g = v; b = n; break;
    case 3: r = wh; g = n; b = v; break;
    case 4: r = n; g = wh; b = v; break;
    case 5: r = v; g = wh; b = n; break;
  }

  return [r * 255, g * 255, b * 255];
}

function hwb2hsl(args) {
  return rgb2hsl(hwb2rgb(args));
}

function hwb2hsv(args) {
  return rgb2hsv(hwb2rgb(args));
}

function hwb2cmyk(args) {
  return rgb2cmyk(hwb2rgb(args));
}

function hwb2keyword(args) {
  return rgb2keyword(hwb2rgb(args));
}

function cmyk2rgb(cmyk) {
  var c = cmyk[0] / 100,
      m = cmyk[1] / 100,
      y = cmyk[2] / 100,
      k = cmyk[3] / 100,
      r, g, b;

  r = 1 - Math.min(1, c * (1 - k) + k);
  g = 1 - Math.min(1, m * (1 - k) + k);
  b = 1 - Math.min(1, y * (1 - k) + k);
  return [r * 255, g * 255, b * 255];
}

function cmyk2hsl(args) {
  return rgb2hsl(cmyk2rgb(args));
}

function cmyk2hsv(args) {
  return rgb2hsv(cmyk2rgb(args));
}

function cmyk2hwb(args) {
  return rgb2hwb(cmyk2rgb(args));
}

function cmyk2keyword(args) {
  return rgb2keyword(cmyk2rgb(args));
}


function xyz2rgb(xyz) {
  var x = xyz[0] / 100,
      y = xyz[1] / 100,
      z = xyz[2] / 100,
      r, g, b;

  r = (x * 3.2406) + (y * -1.5372) + (z * -0.4986);
  g = (x * -0.9689) + (y * 1.8758) + (z * 0.0415);
  b = (x * 0.0557) + (y * -0.2040) + (z * 1.0570);

  // assume sRGB
  r = r > 0.0031308 ? ((1.055 * Math.pow(r, 1.0 / 2.4)) - 0.055)
    : r = (r * 12.92);

  g = g > 0.0031308 ? ((1.055 * Math.pow(g, 1.0 / 2.4)) - 0.055)
    : g = (g * 12.92);

  b = b > 0.0031308 ? ((1.055 * Math.pow(b, 1.0 / 2.4)) - 0.055)
    : b = (b * 12.92);

  r = Math.min(Math.max(0, r), 1);
  g = Math.min(Math.max(0, g), 1);
  b = Math.min(Math.max(0, b), 1);

  return [r * 255, g * 255, b * 255];
}

function xyz2lab(xyz) {
  var x = xyz[0],
      y = xyz[1],
      z = xyz[2],
      l, a, b;

  x /= 95.047;
  y /= 100;
  z /= 108.883;

  x = x > 0.008856 ? Math.pow(x, 1/3) : (7.787 * x) + (16 / 116);
  y = y > 0.008856 ? Math.pow(y, 1/3) : (7.787 * y) + (16 / 116);
  z = z > 0.008856 ? Math.pow(z, 1/3) : (7.787 * z) + (16 / 116);

  l = (116 * y) - 16;
  a = 500 * (x - y);
  b = 200 * (y - z);

  return [l, a, b];
}

function xyz2lch(args) {
  return lab2lch(xyz2lab(args));
}

function lab2xyz(lab) {
  var l = lab[0],
      a = lab[1],
      b = lab[2],
      x, y, z, y2;

  if (l <= 8) {
    y = (l * 100) / 903.3;
    y2 = (7.787 * (y / 100)) + (16 / 116);
  } else {
    y = 100 * Math.pow((l + 16) / 116, 3);
    y2 = Math.pow(y / 100, 1/3);
  }

  x = x / 95.047 <= 0.008856 ? x = (95.047 * ((a / 500) + y2 - (16 / 116))) / 7.787 : 95.047 * Math.pow((a / 500) + y2, 3);

  z = z / 108.883 <= 0.008859 ? z = (108.883 * (y2 - (b / 200) - (16 / 116))) / 7.787 : 108.883 * Math.pow(y2 - (b / 200), 3);

  return [x, y, z];
}

function lab2lch(lab) {
  var l = lab[0],
      a = lab[1],
      b = lab[2],
      hr, h, c;

  hr = Math.atan2(b, a);
  h = hr * 360 / 2 / Math.PI;
  if (h < 0) {
    h += 360;
  }
  c = Math.sqrt(a * a + b * b);
  return [l, c, h];
}

function lab2rgb(args) {
  return xyz2rgb(lab2xyz(args));
}

function lch2lab(lch) {
  var l = lch[0],
      c = lch[1],
      h = lch[2],
      a, b, hr;

  hr = h / 360 * 2 * Math.PI;
  a = c * Math.cos(hr);
  b = c * Math.sin(hr);
  return [l, a, b];
}

function lch2xyz(args) {
  return lab2xyz(lch2lab(args));
}

function lch2rgb(args) {
  return lab2rgb(lch2lab(args));
}

function keyword2rgb(keyword) {
  return cssKeywords[keyword];
}

function keyword2hsl(args) {
  return rgb2hsl(keyword2rgb(args));
}

function keyword2hsv(args) {
  return rgb2hsv(keyword2rgb(args));
}

function keyword2hwb(args) {
  return rgb2hwb(keyword2rgb(args));
}

function keyword2cmyk(args) {
  return rgb2cmyk(keyword2rgb(args));
}

function keyword2lab(args) {
  return rgb2lab(keyword2rgb(args));
}

function keyword2xyz(args) {
  return rgb2xyz(keyword2rgb(args));
}

var cssKeywords = {
  aliceblue:  [240,248,255],
  antiquewhite: [250,235,215],
  aqua: [0,255,255],
  aquamarine: [127,255,212],
  azure:  [240,255,255],
  beige:  [245,245,220],
  bisque: [255,228,196],
  black:  [0,0,0],
  blanchedalmond: [255,235,205],
  blue: [0,0,255],
  blueviolet: [138,43,226],
  brown:  [165,42,42],
  burlywood:  [222,184,135],
  cadetblue:  [95,158,160],
  chartreuse: [127,255,0],
  chocolate:  [210,105,30],
  coral:  [255,127,80],
  cornflowerblue: [100,149,237],
  cornsilk: [255,248,220],
  crimson:  [220,20,60],
  cyan: [0,255,255],
  darkblue: [0,0,139],
  darkcyan: [0,139,139],
  darkgoldenrod:  [184,134,11],
  darkgray: [169,169,169],
  darkgreen:  [0,100,0],
  darkgrey: [169,169,169],
  darkkhaki:  [189,183,107],
  darkmagenta:  [139,0,139],
  darkolivegreen: [85,107,47],
  darkorange: [255,140,0],
  darkorchid: [153,50,204],
  darkred:  [139,0,0],
  darksalmon: [233,150,122],
  darkseagreen: [143,188,143],
  darkslateblue:  [72,61,139],
  darkslategray:  [47,79,79],
  darkslategrey:  [47,79,79],
  darkturquoise:  [0,206,209],
  darkviolet: [148,0,211],
  deeppink: [255,20,147],
  deepskyblue:  [0,191,255],
  dimgray:  [105,105,105],
  dimgrey:  [105,105,105],
  dodgerblue: [30,144,255],
  firebrick:  [178,34,34],
  floralwhite:  [255,250,240],
  forestgreen:  [34,139,34],
  fuchsia:  [255,0,255],
  gainsboro:  [220,220,220],
  ghostwhite: [248,248,255],
  gold: [255,215,0],
  goldenrod:  [218,165,32],
  gray: [128,128,128],
  green:  [0,128,0],
  greenyellow:  [173,255,47],
  grey: [128,128,128],
  honeydew: [240,255,240],
  hotpink:  [255,105,180],
  indianred:  [205,92,92],
  indigo: [75,0,130],
  ivory:  [255,255,240],
  khaki:  [240,230,140],
  lavender: [230,230,250],
  lavenderblush:  [255,240,245],
  lawngreen:  [124,252,0],
  lemonchiffon: [255,250,205],
  lightblue:  [173,216,230],
  lightcoral: [240,128,128],
  lightcyan:  [224,255,255],
  lightgoldenrodyellow: [250,250,210],
  lightgray:  [211,211,211],
  lightgreen: [144,238,144],
  lightgrey:  [211,211,211],
  lightpink:  [255,182,193],
  lightsalmon:  [255,160,122],
  lightseagreen:  [32,178,170],
  lightskyblue: [135,206,250],
  lightslategray: [119,136,153],
  lightslategrey: [119,136,153],
  lightsteelblue: [176,196,222],
  lightyellow:  [255,255,224],
  lime: [0,255,0],
  limegreen:  [50,205,50],
  linen:  [250,240,230],
  magenta:  [255,0,255],
  maroon: [128,0,0],
  mediumaquamarine: [102,205,170],
  mediumblue: [0,0,205],
  mediumorchid: [186,85,211],
  mediumpurple: [147,112,219],
  mediumseagreen: [60,179,113],
  mediumslateblue:  [123,104,238],
  mediumspringgreen:  [0,250,154],
  mediumturquoise:  [72,209,204],
  mediumvioletred:  [199,21,133],
  midnightblue: [25,25,112],
  mintcream:  [245,255,250],
  mistyrose:  [255,228,225],
  moccasin: [255,228,181],
  navajowhite:  [255,222,173],
  navy: [0,0,128],
  oldlace:  [253,245,230],
  olive:  [128,128,0],
  olivedrab:  [107,142,35],
  orange: [255,165,0],
  orangered:  [255,69,0],
  orchid: [218,112,214],
  palegoldenrod:  [238,232,170],
  palegreen:  [152,251,152],
  paleturquoise:  [175,238,238],
  palevioletred:  [219,112,147],
  papayawhip: [255,239,213],
  peachpuff:  [255,218,185],
  peru: [205,133,63],
  pink: [255,192,203],
  plum: [221,160,221],
  powderblue: [176,224,230],
  purple: [128,0,128],
  rebeccapurple: [102, 51, 153],
  red:  [255,0,0],
  rosybrown:  [188,143,143],
  royalblue:  [65,105,225],
  saddlebrown:  [139,69,19],
  salmon: [250,128,114],
  sandybrown: [244,164,96],
  seagreen: [46,139,87],
  seashell: [255,245,238],
  sienna: [160,82,45],
  silver: [192,192,192],
  skyblue:  [135,206,235],
  slateblue:  [106,90,205],
  slategray:  [112,128,144],
  slategrey:  [112,128,144],
  snow: [255,250,250],
  springgreen:  [0,255,127],
  steelblue:  [70,130,180],
  tan:  [210,180,140],
  teal: [0,128,128],
  thistle:  [216,191,216],
  tomato: [255,99,71],
  turquoise:  [64,224,208],
  violet: [238,130,238],
  wheat:  [245,222,179],
  white:  [255,255,255],
  whitesmoke: [245,245,245],
  yellow: [255,255,0],
  yellowgreen:  [154,205,50]
};

var reverseKeywords = {};
for (var key in cssKeywords) {
  reverseKeywords[JSON.stringify(cssKeywords[key])] = key;
}

},{}],5:[function(require,module,exports){
var conversions = require(4);

var convert = function() {
   return new Converter();
}

for (var func in conversions) {
  // export Raw versions
  convert[func + "Raw"] =  (function(func) {
    // accept array or plain args
    return function(arg) {
      if (typeof arg == "number")
        arg = Array.prototype.slice.call(arguments);
      return conversions[func](arg);
    }
  })(func);

  var pair = /(\w+)2(\w+)/.exec(func),
      from = pair[1],
      to = pair[2];

  // export rgb2hsl and ["rgb"]["hsl"]
  convert[from] = convert[from] || {};

  convert[from][to] = convert[func] = (function(func) {
    return function(arg) {
      if (typeof arg == "number")
        arg = Array.prototype.slice.call(arguments);

      var val = conversions[func](arg);
      if (typeof val == "string" || val === undefined)
        return val; // keyword

      for (var i = 0; i < val.length; i++)
        val[i] = Math.round(val[i]);
      return val;
    }
  })(func);
}


/* Converter does lazy conversion and caching */
var Converter = function() {
   this.convs = {};
};

/* Either get the values for a space or
  set the values for a space, depending on args */
Converter.prototype.routeSpace = function(space, args) {
   var values = args[0];
   if (values === undefined) {
      // color.rgb()
      return this.getValues(space);
   }
   // color.rgb(10, 10, 10)
   if (typeof values == "number") {
      values = Array.prototype.slice.call(args);
   }

   return this.setValues(space, values);
};

/* Set the values for a space, invalidating cache */
Converter.prototype.setValues = function(space, values) {
   this.space = space;
   this.convs = {};
   this.convs[space] = values;
   return this;
};

/* Get the values for a space. If there's already
  a conversion for the space, fetch it, otherwise
  compute it */
Converter.prototype.getValues = function(space) {
   var vals = this.convs[space];
   if (!vals) {
      var fspace = this.space,
          from = this.convs[fspace];
      vals = convert[fspace][space](from);

      this.convs[space] = vals;
   }
  return vals;
};

["rgb", "hsl", "hsv", "cmyk", "keyword"].forEach(function(space) {
   Converter.prototype[space] = function(vals) {
      return this.routeSpace(space, arguments);
   }
});

module.exports = convert;
},{"4":4}],6:[function(require,module,exports){
module.exports = {
  "aliceblue": [240, 248, 255],
  "antiquewhite": [250, 235, 215],
  "aqua": [0, 255, 255],
  "aquamarine": [127, 255, 212],
  "azure": [240, 255, 255],
  "beige": [245, 245, 220],
  "bisque": [255, 228, 196],
  "black": [0, 0, 0],
  "blanchedalmond": [255, 235, 205],
  "blue": [0, 0, 255],
  "blueviolet": [138, 43, 226],
  "brown": [165, 42, 42],
  "burlywood": [222, 184, 135],
  "cadetblue": [95, 158, 160],
  "chartreuse": [127, 255, 0],
  "chocolate": [210, 105, 30],
  "coral": [255, 127, 80],
  "cornflowerblue": [100, 149, 237],
  "cornsilk": [255, 248, 220],
  "crimson": [220, 20, 60],
  "cyan": [0, 255, 255],
  "darkblue": [0, 0, 139],
  "darkcyan": [0, 139, 139],
  "darkgoldenrod": [184, 134, 11],
  "darkgray": [169, 169, 169],
  "darkgreen": [0, 100, 0],
  "darkgrey": [169, 169, 169],
  "darkkhaki": [189, 183, 107],
  "darkmagenta": [139, 0, 139],
  "darkolivegreen": [85, 107, 47],
  "darkorange": [255, 140, 0],
  "darkorchid": [153, 50, 204],
  "darkred": [139, 0, 0],
  "darksalmon": [233, 150, 122],
  "darkseagreen": [143, 188, 143],
  "darkslateblue": [72, 61, 139],
  "darkslategray": [47, 79, 79],
  "darkslategrey": [47, 79, 79],
  "darkturquoise": [0, 206, 209],
  "darkviolet": [148, 0, 211],
  "deeppink": [255, 20, 147],
  "deepskyblue": [0, 191, 255],
  "dimgray": [105, 105, 105],
  "dimgrey": [105, 105, 105],
  "dodgerblue": [30, 144, 255],
  "firebrick": [178, 34, 34],
  "floralwhite": [255, 250, 240],
  "forestgreen": [34, 139, 34],
  "fuchsia": [255, 0, 255],
  "gainsboro": [220, 220, 220],
  "ghostwhite": [248, 248, 255],
  "gold": [255, 215, 0],
  "goldenrod": [218, 165, 32],
  "gray": [128, 128, 128],
  "green": [0, 128, 0],
  "greenyellow": [173, 255, 47],
  "grey": [128, 128, 128],
  "honeydew": [240, 255, 240],
  "hotpink": [255, 105, 180],
  "indianred": [205, 92, 92],
  "indigo": [75, 0, 130],
  "ivory": [255, 255, 240],
  "khaki": [240, 230, 140],
  "lavender": [230, 230, 250],
  "lavenderblush": [255, 240, 245],
  "lawngreen": [124, 252, 0],
  "lemonchiffon": [255, 250, 205],
  "lightblue": [173, 216, 230],
  "lightcoral": [240, 128, 128],
  "lightcyan": [224, 255, 255],
  "lightgoldenrodyellow": [250, 250, 210],
  "lightgray": [211, 211, 211],
  "lightgreen": [144, 238, 144],
  "lightgrey": [211, 211, 211],
  "lightpink": [255, 182, 193],
  "lightsalmon": [255, 160, 122],
  "lightseagreen": [32, 178, 170],
  "lightskyblue": [135, 206, 250],
  "lightslategray": [119, 136, 153],
  "lightslategrey": [119, 136, 153],
  "lightsteelblue": [176, 196, 222],
  "lightyellow": [255, 255, 224],
  "lime": [0, 255, 0],
  "limegreen": [50, 205, 50],
  "linen": [250, 240, 230],
  "magenta": [255, 0, 255],
  "maroon": [128, 0, 0],
  "mediumaquamarine": [102, 205, 170],
  "mediumblue": [0, 0, 205],
  "mediumorchid": [186, 85, 211],
  "mediumpurple": [147, 112, 219],
  "mediumseagreen": [60, 179, 113],
  "mediumslateblue": [123, 104, 238],
  "mediumspringgreen": [0, 250, 154],
  "mediumturquoise": [72, 209, 204],
  "mediumvioletred": [199, 21, 133],
  "midnightblue": [25, 25, 112],
  "mintcream": [245, 255, 250],
  "mistyrose": [255, 228, 225],
  "moccasin": [255, 228, 181],
  "navajowhite": [255, 222, 173],
  "navy": [0, 0, 128],
  "oldlace": [253, 245, 230],
  "olive": [128, 128, 0],
  "olivedrab": [107, 142, 35],
  "orange": [255, 165, 0],
  "orangered": [255, 69, 0],
  "orchid": [218, 112, 214],
  "palegoldenrod": [238, 232, 170],
  "palegreen": [152, 251, 152],
  "paleturquoise": [175, 238, 238],
  "palevioletred": [219, 112, 147],
  "papayawhip": [255, 239, 213],
  "peachpuff": [255, 218, 185],
  "peru": [205, 133, 63],
  "pink": [255, 192, 203],
  "plum": [221, 160, 221],
  "powderblue": [176, 224, 230],
  "purple": [128, 0, 128],
  "rebeccapurple": [102, 51, 153],
  "red": [255, 0, 0],
  "rosybrown": [188, 143, 143],
  "royalblue": [65, 105, 225],
  "saddlebrown": [139, 69, 19],
  "salmon": [250, 128, 114],
  "sandybrown": [244, 164, 96],
  "seagreen": [46, 139, 87],
  "seashell": [255, 245, 238],
  "sienna": [160, 82, 45],
  "silver": [192, 192, 192],
  "skyblue": [135, 206, 235],
  "slateblue": [106, 90, 205],
  "slategray": [112, 128, 144],
  "slategrey": [112, 128, 144],
  "snow": [255, 250, 250],
  "springgreen": [0, 255, 127],
  "steelblue": [70, 130, 180],
  "tan": [210, 180, 140],
  "teal": [0, 128, 128],
  "thistle": [216, 191, 216],
  "tomato": [255, 99, 71],
  "turquoise": [64, 224, 208],
  "violet": [238, 130, 238],
  "wheat": [245, 222, 179],
  "white": [255, 255, 255],
  "whitesmoke": [245, 245, 245],
  "yellow": [255, 255, 0],
  "yellowgreen": [154, 205, 50]
};
},{}],7:[function(require,module,exports){
/**
 * @namespace Chart
 */
var Chart = require(27)();

require(26)(Chart);
require(22)(Chart);
require(25)(Chart);
require(21)(Chart);
require(23)(Chart);
require(24)(Chart);
require(28)(Chart);
require(32)(Chart);
require(30)(Chart);
require(31)(Chart);
require(33)(Chart);
require(29)(Chart);
require(34)(Chart);

require(35)(Chart);
require(36)(Chart);
require(37)(Chart);
require(38)(Chart);

require(41)(Chart);
require(39)(Chart);
require(40)(Chart);
require(42)(Chart);
require(43)(Chart);
require(44)(Chart);

// Controllers must be loaded after elements
// See Chart.core.datasetController.dataElementType
require(15)(Chart);
require(16)(Chart);
require(17)(Chart);
require(18)(Chart);
require(19)(Chart);
require(20)(Chart);

require(8)(Chart);
require(9)(Chart);
require(10)(Chart);
require(11)(Chart);
require(12)(Chart);
require(13)(Chart);
require(14)(Chart);

window.Chart = module.exports = Chart;

},{"10":10,"11":11,"12":12,"13":13,"14":14,"15":15,"16":16,"17":17,"18":18,"19":19,"20":20,"21":21,"22":22,"23":23,"24":24,"25":25,"26":26,"27":27,"28":28,"29":29,"30":30,"31":31,"32":32,"33":33,"34":34,"35":35,"36":36,"37":37,"38":38,"39":39,"40":40,"41":41,"42":42,"43":43,"44":44,"8":8,"9":9}],8:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  Chart.Bar = function(context, config) {
    config.type = 'bar';

    return new Chart(context, config);
  };

};
},{}],9:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  Chart.Bubble = function(context, config) {
    config.type = 'bubble';
    return new Chart(context, config);
  };

};
},{}],10:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  Chart.Doughnut = function(context, config) {
    config.type = 'doughnut';

    return new Chart(context, config);
  };

};
},{}],11:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  Chart.Line = function(context, config) {
    config.type = 'line';

    return new Chart(context, config);
  };

};
},{}],12:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  Chart.PolarArea = function(context, config) {
    config.type = 'polarArea';

    return new Chart(context, config);
  };

};
},{}],13:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  Chart.Radar = function(context, config) {
    config.options = Chart.helpers.configMerge({ aspectRatio: 1 }, config.options);
    config.type = 'radar';

    return new Chart(context, config);
  };

};

},{}],14:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var defaultConfig = {
    hover: {
      mode: 'single'
    },

    scales: {
      xAxes: [{
        type: "linear", // scatter should not use a category axis
        position: "bottom",
        id: "x-axis-1" // need an ID so datasets can reference the scale
      }],
      yAxes: [{
        type: "linear",
        position: "left",
        id: "y-axis-1"
      }]
    },

    tooltips: {
      callbacks: {
        title: function() {
          // Title doesn't make sense for scatter since we format the data as a point
          return '';
        },
        label: function(tooltipItem) {
          return '(' + tooltipItem.xLabel + ', ' + tooltipItem.yLabel + ')';
        }
      }
    }
  };

  // Register the default config for this type
  Chart.defaults.scatter = defaultConfig;

  // Scatter charts use line controllers
  Chart.controllers.scatter = Chart.controllers.line;

  Chart.Scatter = function(context, config) {
    config.type = 'scatter';
    return new Chart(context, config);
  };

};
},{}],15:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.bar = {
    hover: {
      mode: "label"
    },

    scales: {
      xAxes: [{
        type: "category",

        // Specific to Bar Controller
        categoryPercentage: 0.8,
        barPercentage: 0.9,

        // grid line settings
        gridLines: {
          offsetGridLines: true
        }
      }],
      yAxes: [{
        type: "linear"
      }]
    }
  };

  Chart.controllers.bar = Chart.DatasetController.extend({

    dataElementType: Chart.elements.Rectangle,

    initialize: function(chart, datasetIndex) {
      Chart.DatasetController.prototype.initialize.call(this, chart, datasetIndex);

      // Use this to indicate that this is a bar dataset.
      this.getMeta().bar = true;
    },

    // Get the number of datasets that display bars. We use this to correctly calculate the bar width
    getBarCount: function() {
      var me = this;
      var barCount = 0;
      helpers.each(me.chart.data.datasets, function(dataset, datasetIndex) {
        var meta = me.chart.getDatasetMeta(datasetIndex);
        if (meta.bar && me.chart.isDatasetVisible(datasetIndex)) {
          ++barCount;
        }
      }, me);
      return barCount;
    },

    update: function(reset) {
      var me = this;
      helpers.each(me.getMeta().data, function(rectangle, index) {
        me.updateElement(rectangle, index, reset);
      }, me);
    },

    updateElement: function(rectangle, index, reset) {
      var me = this;
      var meta = me.getMeta();
      var xScale = me.getScaleForId(meta.xAxisID);
      var yScale = me.getScaleForId(meta.yAxisID);
      var scaleBase = yScale.getBasePixel();
      var rectangleElementOptions = me.chart.options.elements.rectangle;
      var custom = rectangle.custom || {};
      var dataset = me.getDataset();

      helpers.extend(rectangle, {
        // Utility
        _xScale: xScale,
        _yScale: yScale,
        _datasetIndex: me.index,
        _index: index,

        // Desired view properties
        _model: {
          x: me.calculateBarX(index, me.index),
          y: reset ? scaleBase : me.calculateBarY(index, me.index),

          // Tooltip
          label: me.chart.data.labels[index],
          datasetLabel: dataset.label,

          // Appearance
          base: reset ? scaleBase : me.calculateBarBase(me.index, index),
          width: me.calculateBarWidth(index),
          backgroundColor: custom.backgroundColor ? custom.backgroundColor : helpers.getValueAtIndexOrDefault(dataset.backgroundColor, index, rectangleElementOptions.backgroundColor),
          borderSkipped: custom.borderSkipped ? custom.borderSkipped : rectangleElementOptions.borderSkipped,
          borderColor: custom.borderColor ? custom.borderColor : helpers.getValueAtIndexOrDefault(dataset.borderColor, index, rectangleElementOptions.borderColor),
          borderWidth: custom.borderWidth ? custom.borderWidth : helpers.getValueAtIndexOrDefault(dataset.borderWidth, index, rectangleElementOptions.borderWidth)
        }
      });
      rectangle.pivot();
    },

    calculateBarBase: function(datasetIndex, index) {
      var me = this;
      var meta = me.getMeta();
      var yScale = me.getScaleForId(meta.yAxisID);
      var base = 0;

      if (yScale.options.stacked) {
        var chart = me.chart;
        var datasets = chart.data.datasets;
        var value = Number(datasets[datasetIndex].data[index]);

        for (var i = 0; i < datasetIndex; i++) {
          var currentDs = datasets[i];
          var currentDsMeta = chart.getDatasetMeta(i);
          if (currentDsMeta.bar && currentDsMeta.yAxisID === yScale.id && chart.isDatasetVisible(i)) {
            var currentVal = Number(currentDs.data[index]);
            base += value < 0 ? Math.min(currentVal, 0) : Math.max(currentVal, 0);
          }
        }

        return yScale.getPixelForValue(base);
      }

      return yScale.getBasePixel();
    },

    getRuler: function(index) {
      var me = this;
      var meta = me.getMeta();
      var xScale = me.getScaleForId(meta.xAxisID);
      var datasetCount = me.getBarCount();

      var tickWidth;

      if (xScale.options.type === 'category') {
        tickWidth = xScale.getPixelForTick(index + 1) - xScale.getPixelForTick(index);
      } else {
        // Average width
        tickWidth = xScale.width / xScale.ticks.length;
      }
      var categoryWidth = tickWidth * xScale.options.categoryPercentage;
      var categorySpacing = (tickWidth - (tickWidth * xScale.options.categoryPercentage)) / 2;
      var fullBarWidth = categoryWidth / datasetCount;

      if (xScale.ticks.length !== me.chart.data.labels.length) {
          var perc = xScale.ticks.length / me.chart.data.labels.length;
          fullBarWidth = fullBarWidth * perc;
      }

      var barWidth = fullBarWidth * xScale.options.barPercentage;
      var barSpacing = fullBarWidth - (fullBarWidth * xScale.options.barPercentage);

      return {
        datasetCount: datasetCount,
        tickWidth: tickWidth,
        categoryWidth: categoryWidth,
        categorySpacing: categorySpacing,
        fullBarWidth: fullBarWidth,
        barWidth: barWidth,
        barSpacing: barSpacing
      };
    },

    calculateBarWidth: function(index) {
      var xScale = this.getScaleForId(this.getMeta().xAxisID);
      if (xScale.options.barThickness) {
        return xScale.options.barThickness;
      }
      var ruler = this.getRuler(index);
      return xScale.options.stacked ? ruler.categoryWidth : ruler.barWidth;
    },

    // Get bar index from the given dataset index accounting for the fact that not all bars are visible
    getBarIndex: function(datasetIndex) {
      var barIndex = 0;
      var meta, j;

      for (j = 0; j < datasetIndex; ++j) {
        meta = this.chart.getDatasetMeta(j);
        if (meta.bar && this.chart.isDatasetVisible(j)) {
          ++barIndex;
        }
      }

      return barIndex;
    },

    calculateBarX: function(index, datasetIndex) {
      var me = this;
      var meta = me.getMeta();
      var xScale = me.getScaleForId(meta.xAxisID);
      var barIndex = me.getBarIndex(datasetIndex);

      var ruler = me.getRuler(index);
      var leftTick = xScale.getPixelForValue(null, index, datasetIndex, me.chart.isCombo);
      leftTick -= me.chart.isCombo ? (ruler.tickWidth / 2) : 0;

      if (xScale.options.stacked) {
        return leftTick + (ruler.categoryWidth / 2) + ruler.categorySpacing;
      }

      return leftTick +
        (ruler.barWidth / 2) +
        ruler.categorySpacing +
        (ruler.barWidth * barIndex) +
        (ruler.barSpacing / 2) +
        (ruler.barSpacing * barIndex);
    },

    calculateBarY: function(index, datasetIndex) {
      var me = this;
      var meta = me.getMeta();
      var yScale = me.getScaleForId(meta.yAxisID);
      var value = Number(me.getDataset().data[index]);

      if (yScale.options.stacked) {

        var sumPos = 0,
          sumNeg = 0;

        for (var i = 0; i < datasetIndex; i++) {
          var ds = me.chart.data.datasets[i];
          var dsMeta = me.chart.getDatasetMeta(i);
          if (dsMeta.bar && dsMeta.yAxisID === yScale.id && me.chart.isDatasetVisible(i)) {
            var stackedVal = Number(ds.data[index]);
            if (stackedVal < 0) {
              sumNeg += stackedVal || 0;
            } else {
              sumPos += stackedVal || 0;
            }
          }
        }

        if (value < 0) {
          return yScale.getPixelForValue(sumNeg + value);
        } else {
          return yScale.getPixelForValue(sumPos + value);
        }
      }

      return yScale.getPixelForValue(value);
    },

    draw: function(ease) {
      var me = this;
      var easingDecimal = ease || 1;
      helpers.each(me.getMeta().data, function(rectangle, index) {
        var d = me.getDataset().data[index];
        if (d !== null && d !== undefined && !isNaN(d)) {
          rectangle.transition(easingDecimal).draw();
        }
      }, me);
    },

    setHoverStyle: function(rectangle) {
      var dataset = this.chart.data.datasets[rectangle._datasetIndex];
      var index = rectangle._index;

      var custom = rectangle.custom || {};
      var model = rectangle._model;
      model.backgroundColor = custom.hoverBackgroundColor ? custom.hoverBackgroundColor : helpers.getValueAtIndexOrDefault(dataset.hoverBackgroundColor, index, helpers.getHoverColor(model.backgroundColor));
      model.borderColor = custom.hoverBorderColor ? custom.hoverBorderColor : helpers.getValueAtIndexOrDefault(dataset.hoverBorderColor, index, helpers.getHoverColor(model.borderColor));
      model.borderWidth = custom.hoverBorderWidth ? custom.hoverBorderWidth : helpers.getValueAtIndexOrDefault(dataset.hoverBorderWidth, index, model.borderWidth);
    },

    removeHoverStyle: function(rectangle) {
      var dataset = this.chart.data.datasets[rectangle._datasetIndex];
      var index = rectangle._index;
      var custom = rectangle.custom || {};
      var model = rectangle._model;
      var rectangleElementOptions = this.chart.options.elements.rectangle;

      model.backgroundColor = custom.backgroundColor ? custom.backgroundColor : helpers.getValueAtIndexOrDefault(dataset.backgroundColor, index, rectangleElementOptions.backgroundColor);
      model.borderColor = custom.borderColor ? custom.borderColor : helpers.getValueAtIndexOrDefault(dataset.borderColor, index, rectangleElementOptions.borderColor);
      model.borderWidth = custom.borderWidth ? custom.borderWidth : helpers.getValueAtIndexOrDefault(dataset.borderWidth, index, rectangleElementOptions.borderWidth);
    }

  });


  // including horizontalBar in the bar file, instead of a file of its own
  // it extends bar (like pie extends doughnut)
  Chart.defaults.horizontalBar = {
    hover: {
      mode: "label"
    },

    scales: {
      xAxes: [{
        type: "linear",
        position: "bottom"
      }],
      yAxes: [{
        position: "left",
        type: "category",

        // Specific to Horizontal Bar Controller
        categoryPercentage: 0.8,
        barPercentage: 0.9,

        // grid line settings
        gridLines: {
          offsetGridLines: true
        }
      }]
    },
    elements: {
      rectangle: {
        borderSkipped: 'left'
      }
    },
    tooltips: {
      callbacks: {
        title: function(tooltipItems, data) {
          // Pick first xLabel for now
          var title = '';

          if (tooltipItems.length > 0) {
            if (tooltipItems[0].yLabel) {
              title = tooltipItems[0].yLabel;
            } else if (data.labels.length > 0 && tooltipItems[0].index < data.labels.length) {
              title = data.labels[tooltipItems[0].index];
            }
          }

          return title;
        },
        label: function(tooltipItem, data) {
          var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
        return datasetLabel + ': ' + tooltipItem.xLabel;
        }
      }
    }
  };

  Chart.controllers.horizontalBar = Chart.controllers.bar.extend({
    updateElement: function(rectangle, index, reset) {
      var me = this;
      var meta = me.getMeta();
      var xScale = me.getScaleForId(meta.xAxisID);
      var yScale = me.getScaleForId(meta.yAxisID);
      var scaleBase = xScale.getBasePixel();
      var custom = rectangle.custom || {};
      var dataset = me.getDataset();
      var rectangleElementOptions = me.chart.options.elements.rectangle;

      helpers.extend(rectangle, {
        // Utility
        _xScale: xScale,
        _yScale: yScale,
        _datasetIndex: me.index,
        _index: index,

        // Desired view properties
        _model: {
          x: reset ? scaleBase : me.calculateBarX(index, me.index),
          y: me.calculateBarY(index, me.index),

          // Tooltip
          label: me.chart.data.labels[index],
          datasetLabel: dataset.label,

          // Appearance
          base: reset ? scaleBase : me.calculateBarBase(me.index, index),
          height: me.calculateBarHeight(index),
          backgroundColor: custom.backgroundColor ? custom.backgroundColor : helpers.getValueAtIndexOrDefault(dataset.backgroundColor, index, rectangleElementOptions.backgroundColor),
          borderSkipped: custom.borderSkipped ? custom.borderSkipped : rectangleElementOptions.borderSkipped,
          borderColor: custom.borderColor ? custom.borderColor : helpers.getValueAtIndexOrDefault(dataset.borderColor, index, rectangleElementOptions.borderColor),
          borderWidth: custom.borderWidth ? custom.borderWidth : helpers.getValueAtIndexOrDefault(dataset.borderWidth, index, rectangleElementOptions.borderWidth)
        },

        draw: function () {
          var ctx = this._chart.ctx;
          var vm = this._view;

          var halfHeight = vm.height / 2,
            topY = vm.y - halfHeight,
            bottomY = vm.y + halfHeight,
            right = vm.base - (vm.base - vm.x),
            halfStroke = vm.borderWidth / 2;

          // Canvas doesn't allow us to stroke inside the width so we can
          // adjust the sizes to fit if we're setting a stroke on the line
          if (vm.borderWidth) {
            topY += halfStroke;
            bottomY -= halfStroke;
            right += halfStroke;
          }

          ctx.beginPath();

          ctx.fillStyle = vm.backgroundColor;
          ctx.strokeStyle = vm.borderColor;
          ctx.lineWidth = vm.borderWidth;

          // Corner points, from bottom-left to bottom-right clockwise
          // | 1 2 |
          // | 0 3 |
          var corners = [
            [vm.base, bottomY],
            [vm.base, topY],
            [right, topY],
            [right, bottomY]
          ];

          // Find first (starting) corner with fallback to 'bottom'
          var borders = ['bottom', 'left', 'top', 'right'];
          var startCorner = borders.indexOf(vm.borderSkipped, 0);
          if (startCorner === -1)
            startCorner = 0;

          function cornerAt(index) {
            return corners[(startCorner + index) % 4];
          }

          // Draw rectangle from 'startCorner'
          ctx.moveTo.apply(ctx, cornerAt(0));
          for (var i = 1; i < 4; i++)
            ctx.lineTo.apply(ctx, cornerAt(i));

          ctx.fill();
          if (vm.borderWidth) {
            ctx.stroke();
          }
        },

        inRange: function (mouseX, mouseY) {
          var vm = this._view;
          var inRange = false;

          if (vm) {
            if (vm.x < vm.base) {
              inRange = (mouseY >= vm.y - vm.height / 2 && mouseY <= vm.y + vm.height / 2) && (mouseX >= vm.x && mouseX <= vm.base);
            } else {
              inRange = (mouseY >= vm.y - vm.height / 2 && mouseY <= vm.y + vm.height / 2) && (mouseX >= vm.base && mouseX <= vm.x);
            }
          }

          return inRange;
        }
      });

      rectangle.pivot();
    },

    calculateBarBase: function (datasetIndex, index) {
      var me = this;
      var meta = me.getMeta();
      var xScale = me.getScaleForId(meta.xAxisID);
      var base = 0;

      if (xScale.options.stacked) {
        var chart = me.chart;
        var datasets = chart.data.datasets;
        var value = Number(datasets[datasetIndex].data[index]);

        for (var i = 0; i < datasetIndex; i++) {
          var currentDs = datasets[i];
          var currentDsMeta = chart.getDatasetMeta(i);
          if (currentDsMeta.bar && currentDsMeta.xAxisID === xScale.id && chart.isDatasetVisible(i)) {
            var currentVal = Number(currentDs.data[index]);
            base += value < 0 ? Math.min(currentVal, 0) : Math.max(currentVal, 0);
          }
        }

        return xScale.getPixelForValue(base);
      }

      return xScale.getBasePixel();
    },

    getRuler: function (index) {
      var me = this;
      var meta = me.getMeta();
      var yScale = me.getScaleForId(meta.yAxisID);
      var datasetCount = me.getBarCount();

      var tickHeight;
      if (yScale.options.type === 'category') {
        tickHeight = yScale.getPixelForTick(index + 1) - yScale.getPixelForTick(index);
      } else {
        // Average width
        tickHeight = yScale.width / yScale.ticks.length;
      }
      var categoryHeight = tickHeight * yScale.options.categoryPercentage;
      var categorySpacing = (tickHeight - (tickHeight * yScale.options.categoryPercentage)) / 2;
      var fullBarHeight = categoryHeight / datasetCount;

      if (yScale.ticks.length !== me.chart.data.labels.length) {
        var perc = yScale.ticks.length / me.chart.data.labels.length;
        fullBarHeight = fullBarHeight * perc;
      }

      var barHeight = fullBarHeight * yScale.options.barPercentage;
      var barSpacing = fullBarHeight - (fullBarHeight * yScale.options.barPercentage);

      return {
        datasetCount: datasetCount,
        tickHeight: tickHeight,
        categoryHeight: categoryHeight,
        categorySpacing: categorySpacing,
        fullBarHeight: fullBarHeight,
        barHeight: barHeight,
        barSpacing: barSpacing,
      };
    },

    calculateBarHeight: function (index) {
      var me = this;
      var yScale = me.getScaleForId(me.getMeta().yAxisID);
      if (yScale.options.barThickness) {
        return yScale.options.barThickness;
      }
      var ruler = me.getRuler(index);
      return yScale.options.stacked ? ruler.categoryHeight : ruler.barHeight;
    },

    calculateBarX: function (index, datasetIndex) {
      var me = this;
      var meta = me.getMeta();
      var xScale = me.getScaleForId(meta.xAxisID);
      var value = Number(me.getDataset().data[index]);

      if (xScale.options.stacked) {

        var sumPos = 0,
          sumNeg = 0;

        for (var i = 0; i < datasetIndex; i++) {
          var ds = me.chart.data.datasets[i];
          var dsMeta = me.chart.getDatasetMeta(i);
          if (dsMeta.bar && dsMeta.xAxisID === xScale.id && me.chart.isDatasetVisible(i)) {
            var stackedVal = Number(ds.data[index]);
            if (stackedVal < 0) {
              sumNeg += stackedVal || 0;
            } else {
              sumPos += stackedVal || 0;
            }
          }
        }

        if (value < 0) {
          return xScale.getPixelForValue(sumNeg + value);
        } else {
          return xScale.getPixelForValue(sumPos + value);
        }
      }

      return xScale.getPixelForValue(value);
    },

    calculateBarY: function (index, datasetIndex) {
      var me = this;
      var meta = me.getMeta();
      var yScale = me.getScaleForId(meta.yAxisID);
      var barIndex = me.getBarIndex(datasetIndex);

      var ruler = me.getRuler(index);
      var topTick = yScale.getPixelForValue(null, index, datasetIndex, me.chart.isCombo);
      topTick -= me.chart.isCombo ? (ruler.tickHeight / 2) : 0;

      if (yScale.options.stacked) {
        return topTick + (ruler.categoryHeight / 2) + ruler.categorySpacing;
      }

      return topTick +
        (ruler.barHeight / 2) +
        ruler.categorySpacing +
        (ruler.barHeight * barIndex) +
        (ruler.barSpacing / 2) +
        (ruler.barSpacing * barIndex);
    }
  });
};

},{}],16:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.bubble = {
    hover: {
      mode: "single"
    },

    scales: {
      xAxes: [{
        type: "linear", // bubble should probably use a linear scale by default
        position: "bottom",
        id: "x-axis-0" // need an ID so datasets can reference the scale
      }],
      yAxes: [{
        type: "linear",
        position: "left",
        id: "y-axis-0"
      }]
    },

    tooltips: {
      callbacks: {
        title: function() {
          // Title doesn't make sense for scatter since we format the data as a point
          return '';
        },
        label: function(tooltipItem, data) {
          var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
          var dataPoint = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
          return datasetLabel + ': (' + dataPoint.x + ', ' + dataPoint.y + ', ' + dataPoint.r + ')';
        }
      }
    }
  };

  Chart.controllers.bubble = Chart.DatasetController.extend({

    dataElementType: Chart.elements.Point,

    update: function(reset) {
      var me = this;
      var meta = me.getMeta();
      var points = meta.data;

      // Update Points
      helpers.each(points, function(point, index) {
        me.updateElement(point, index, reset);
      });
    },

    updateElement: function(point, index, reset) {
      var me = this;
      var meta = me.getMeta();
      var xScale = me.getScaleForId(meta.xAxisID);
      var yScale = me.getScaleForId(meta.yAxisID);

      var custom = point.custom || {};
      var dataset = me.getDataset();
      var data = dataset.data[index];
      var pointElementOptions = me.chart.options.elements.point;
      var dsIndex = me.index;

      helpers.extend(point, {
        // Utility
        _xScale: xScale,
        _yScale: yScale,
        _datasetIndex: dsIndex,
        _index: index,

        // Desired view properties
        _model: {
          x: reset ? xScale.getPixelForDecimal(0.5) : xScale.getPixelForValue(typeof data === 'object' ? data : NaN, index, dsIndex, me.chart.isCombo),
          y: reset ? yScale.getBasePixel() : yScale.getPixelForValue(data, index, dsIndex),
          // Appearance
          radius: reset ? 0 : custom.radius ? custom.radius : me.getRadius(data),

          // Tooltip
          hitRadius: custom.hitRadius ? custom.hitRadius : helpers.getValueAtIndexOrDefault(dataset.hitRadius, index, pointElementOptions.hitRadius)
        }
      });

      // Trick to reset the styles of the point
      Chart.DatasetController.prototype.removeHoverStyle.call(me, point, pointElementOptions);

      var model = point._model;
      model.skip = custom.skip ? custom.skip : (isNaN(model.x) || isNaN(model.y));

      point.pivot();
    },

    getRadius: function(value) {
      return value.r || this.chart.options.elements.point.radius;
    },

    setHoverStyle: function(point) {
      var me = this;
      Chart.DatasetController.prototype.setHoverStyle.call(me, point);

      // Radius
      var dataset = me.chart.data.datasets[point._datasetIndex];
      var index = point._index;
      var custom = point.custom || {};
      var model = point._model;
      model.radius = custom.hoverRadius ? custom.hoverRadius : (helpers.getValueAtIndexOrDefault(dataset.hoverRadius, index, me.chart.options.elements.point.hoverRadius)) + me.getRadius(dataset.data[index]);
    },

    removeHoverStyle: function(point) {
      var me = this;
      Chart.DatasetController.prototype.removeHoverStyle.call(me, point, me.chart.options.elements.point);

      var dataVal = me.chart.data.datasets[point._datasetIndex].data[point._index];
      var custom = point.custom || {};
      var model = point._model;

      model.radius = custom.radius ? custom.radius : me.getRadius(dataVal);
    }
  });
};

},{}],17:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers,
    defaults = Chart.defaults;

  defaults.doughnut = {
    animation: {
      //Boolean - Whether we animate the rotation of the Doughnut
      animateRotate: true,
      //Boolean - Whether we animate scaling the Doughnut from the centre
      animateScale: false
    },
    aspectRatio: 1,
    hover: {
      mode: 'single'
    },
    legendCallback: function(chart) {
      var text = [];
      text.push('<ul class="' + chart.id + '-legend">');

      var data = chart.data;
      var datasets = data.datasets;
      var labels = data.labels;

      if (datasets.length) {
        for (var i = 0; i < datasets[0].data.length; ++i) {
          text.push('<li><span style="background-color:' + datasets[0].backgroundColor[i] + '"></span>');
          if (labels[i]) {
            text.push(labels[i]);
          }
          text.push('</li>');
        }
      }

      text.push('</ul>');
      return text.join("");
    },
    legend: {
      labels: {
        generateLabels: function(chart) {
          var data = chart.data;
          if (data.labels.length && data.datasets.length) {
            return data.labels.map(function(label, i) {
              var meta = chart.getDatasetMeta(0);
              var ds = data.datasets[0];
              var arc = meta.data[i];
              var custom = arc.custom || {};
              var getValueAtIndexOrDefault = helpers.getValueAtIndexOrDefault;
              var arcOpts = chart.options.elements.arc;
              var fill = custom.backgroundColor ? custom.backgroundColor : getValueAtIndexOrDefault(ds.backgroundColor, i, arcOpts.backgroundColor);
              var stroke = custom.borderColor ? custom.borderColor : getValueAtIndexOrDefault(ds.borderColor, i, arcOpts.borderColor);
              var bw = custom.borderWidth ? custom.borderWidth : getValueAtIndexOrDefault(ds.borderWidth, i, arcOpts.borderWidth);

              return {
                text: label,
                fillStyle: fill,
                strokeStyle: stroke,
                lineWidth: bw,
                hidden: isNaN(ds.data[i]) || meta.data[i].hidden,

                // Extra data used for toggling the correct item
                index: i
              };
            });
          } else {
            return [];
          }
        }
      },

      onClick: function(e, legendItem) {
        var index = legendItem.index;
        var chart = this.chart;
        var i, ilen, meta;

        for (i = 0, ilen = (chart.data.datasets || []).length; i < ilen; ++i) {
          meta = chart.getDatasetMeta(i);
          meta.data[index].hidden = !meta.data[index].hidden;
        }

        chart.update();
      }
    },

    //The percentage of the chart that we cut out of the middle.
    cutoutPercentage: 50,

    //The rotation of the chart, where the first data arc begins.
    rotation: Math.PI * -0.5,

    //The total circumference of the chart.
    circumference: Math.PI * 2.0,

    // Need to override these to give a nice default
    tooltips: {
      callbacks: {
        title: function() {
          return '';
        },
        label: function(tooltipItem, data) {
          return data.labels[tooltipItem.index] + ': ' + data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
        }
      }
    }
  };

  defaults.pie = helpers.clone(defaults.doughnut);
  helpers.extend(defaults.pie, {
    cutoutPercentage: 0
  });


  Chart.controllers.doughnut = Chart.controllers.pie = Chart.DatasetController.extend({

    dataElementType: Chart.elements.Arc,

    linkScales: helpers.noop,

    // Get index of the dataset in relation to the visible datasets. This allows determining the inner and outer radius correctly
    getRingIndex: function(datasetIndex) {
      var ringIndex = 0;

      for (var j = 0; j < datasetIndex; ++j) {
        if (this.chart.isDatasetVisible(j)) {
          ++ringIndex;
        }
      }

      return ringIndex;
    },

    update: function(reset) {
      var me = this;
      var chart = me.chart,
        chartArea = chart.chartArea,
        opts = chart.options,
        arcOpts = opts.elements.arc,
        availableWidth = chartArea.right - chartArea.left - arcOpts.borderWidth,
        availableHeight = chartArea.bottom - chartArea.top - arcOpts.borderWidth,
        minSize = Math.min(availableWidth, availableHeight),
        offset = {
          x: 0,
          y: 0
        },
        meta = me.getMeta(),
        cutoutPercentage = opts.cutoutPercentage,
        circumference = opts.circumference;

      // If the chart's circumference isn't a full circle, calculate minSize as a ratio of the width/height of the arc
      if (circumference < Math.PI * 2.0) {
        var startAngle = opts.rotation % (Math.PI * 2.0);
        startAngle += Math.PI * 2.0 * (startAngle >= Math.PI ? -1 : startAngle < -Math.PI ? 1 : 0);
        var endAngle = startAngle + circumference;
        var start = {x: Math.cos(startAngle), y: Math.sin(startAngle)};
        var end = {x: Math.cos(endAngle), y: Math.sin(endAngle)};
        var contains0 = (startAngle <= 0 && 0 <= endAngle) || (startAngle <= Math.PI * 2.0 && Math.PI * 2.0 <= endAngle);
        var contains90 = (startAngle <= Math.PI * 0.5 && Math.PI * 0.5 <= endAngle) || (startAngle <= Math.PI * 2.5 && Math.PI * 2.5 <= endAngle);
        var contains180 = (startAngle <= -Math.PI && -Math.PI <= endAngle) || (startAngle <= Math.PI && Math.PI <= endAngle);
        var contains270 = (startAngle <= -Math.PI * 0.5 && -Math.PI * 0.5 <= endAngle) || (startAngle <= Math.PI * 1.5 && Math.PI * 1.5 <= endAngle);
        var cutout = cutoutPercentage / 100.0;
        var min = {x: contains180 ? -1 : Math.min(start.x * (start.x < 0 ? 1 : cutout), end.x * (end.x < 0 ? 1 : cutout)), y: contains270 ? -1 : Math.min(start.y * (start.y < 0 ? 1 : cutout), end.y * (end.y < 0 ? 1 : cutout))};
        var max = {x: contains0 ? 1 : Math.max(start.x * (start.x > 0 ? 1 : cutout), end.x * (end.x > 0 ? 1 : cutout)), y: contains90 ? 1 : Math.max(start.y * (start.y > 0 ? 1 : cutout), end.y * (end.y > 0 ? 1 : cutout))};
        var size = {width: (max.x - min.x) * 0.5, height: (max.y - min.y) * 0.5};
        minSize = Math.min(availableWidth / size.width, availableHeight / size.height);
        offset = {x: (max.x + min.x) * -0.5, y: (max.y + min.y) * -0.5};
      }
            chart.borderWidth = me.getMaxBorderWidth(meta.data);

      chart.outerRadius = Math.max((minSize - chart.borderWidth) / 2, 0);
      chart.innerRadius = Math.max(cutoutPercentage ? (chart.outerRadius / 100) * (cutoutPercentage) : 1, 0);
      chart.radiusLength = (chart.outerRadius - chart.innerRadius) / chart.getVisibleDatasetCount();
      chart.offsetX = offset.x * chart.outerRadius;
      chart.offsetY = offset.y * chart.outerRadius;

      meta.total = me.calculateTotal();

      me.outerRadius = chart.outerRadius - (chart.radiusLength * me.getRingIndex(me.index));
      me.innerRadius = me.outerRadius - chart.radiusLength;

      helpers.each(meta.data, function(arc, index) {
        me.updateElement(arc, index, reset);
      });
    },

    updateElement: function(arc, index, reset) {
      var me = this;
      var chart = me.chart,
        chartArea = chart.chartArea,
        opts = chart.options,
        animationOpts = opts.animation,
        centerX = (chartArea.left + chartArea.right) / 2,
        centerY = (chartArea.top + chartArea.bottom) / 2,
        startAngle = opts.rotation, // non reset case handled later
        endAngle = opts.rotation, // non reset case handled later
        dataset = me.getDataset(),
        circumference = reset && animationOpts.animateRotate ? 0 : arc.hidden ? 0 : me.calculateCircumference(dataset.data[index]) * (opts.circumference / (2.0 * Math.PI)),
        innerRadius = reset && animationOpts.animateScale ? 0 : me.innerRadius,
        outerRadius = reset && animationOpts.animateScale ? 0 : me.outerRadius,
        valueAtIndexOrDefault = helpers.getValueAtIndexOrDefault;

      helpers.extend(arc, {
        // Utility
        _datasetIndex: me.index,
        _index: index,

        // Desired view properties
        _model: {
          x: centerX + chart.offsetX,
          y: centerY + chart.offsetY,
          startAngle: startAngle,
          endAngle: endAngle,
          circumference: circumference,
          outerRadius: outerRadius,
          innerRadius: innerRadius,
          label: valueAtIndexOrDefault(dataset.label, index, chart.data.labels[index])
        }
      });

      var model = arc._model;
      // Resets the visual styles
      this.removeHoverStyle(arc);

      // Set correct angles if not resetting
      if (!reset || !animationOpts.animateRotate) {
        if (index === 0) {
          model.startAngle = opts.rotation;
        } else {
          model.startAngle = me.getMeta().data[index - 1]._model.endAngle;
        }

        model.endAngle = model.startAngle + model.circumference;
      }

      arc.pivot();
    },

    removeHoverStyle: function(arc) {
      Chart.DatasetController.prototype.removeHoverStyle.call(this, arc, this.chart.options.elements.arc);
    },

    calculateTotal: function() {
      var dataset = this.getDataset();
      var meta = this.getMeta();
      var total = 0;
      var value;

      helpers.each(meta.data, function(element, index) {
        value = dataset.data[index];
        if (!isNaN(value) && !element.hidden) {
          total += Math.abs(value);
        }
      });

      return total;
    },

    calculateCircumference: function(value) {
      var total = this.getMeta().total;
      if (total > 0 && !isNaN(value)) {
        return (Math.PI * 2.0) * (value / total);
      } else {
        return 0;
      }
    },

    //gets the max border or hover width to properly scale pie charts
        getMaxBorderWidth: function (elements) {
            var max = 0,
        index = this.index,
        length = elements.length,
        borderWidth,
        hoverWidth;

            for (var i = 0; i < length; i++) {
                borderWidth = elements[i]._model ? elements[i]._model.borderWidth : 0;
                hoverWidth = elements[i]._chart ? elements[i]._chart.config.data.datasets[index].hoverBorderWidth : 0;

                max = borderWidth > max ? borderWidth : max;
                max = hoverWidth > max ? hoverWidth : max;
            }
            return max;
        }
  });
};

},{}],18:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.line = {
    showLines: true,
    spanGaps: false,

    hover: {
      mode: "label"
    },

    scales: {
      xAxes: [{
        type: "category",
        id: 'x-axis-0'
      }],
      yAxes: [{
        type: "linear",
        id: 'y-axis-0'
      }]
    }
  };

  function lineEnabled(dataset, options) {
    return helpers.getValueOrDefault(dataset.showLine, options.showLines);
  }

  Chart.controllers.line = Chart.DatasetController.extend({

    datasetElementType: Chart.elements.Line,

    dataElementType: Chart.elements.Point,

    addElementAndReset: function(index) {
      var me = this;
      var options = me.chart.options;
      var meta = me.getMeta();

      Chart.DatasetController.prototype.addElementAndReset.call(me, index);

      // Make sure bezier control points are updated
      if (lineEnabled(me.getDataset(), options) && meta.dataset._model.tension !== 0) {
        me.updateBezierControlPoints();
      }
    },

    update: function(reset) {
      var me = this;
      var meta = me.getMeta();
      var line = meta.dataset;
      var points = meta.data || [];
      var options = me.chart.options;
      var lineElementOptions = options.elements.line;
      var scale = me.getScaleForId(meta.yAxisID);
      var i, ilen, custom;
      var dataset = me.getDataset();
      var showLine = lineEnabled(dataset, options);

      // Update Line
      if (showLine) {
        custom = line.custom || {};

        // Compatibility: If the properties are defined with only the old name, use those values
        if ((dataset.tension !== undefined) && (dataset.lineTension === undefined)) {
          dataset.lineTension = dataset.tension;
        }

        // Utility
        line._scale = scale;
        line._datasetIndex = me.index;
        // Data
        line._children = points;
        // Model
        line._model = {
          // Appearance
          // The default behavior of lines is to break at null values, according
          // to https://github.com/chartjs/Chart.js/issues/2435#issuecomment-216718158
          // This option gives linse the ability to span gaps
          spanGaps: dataset.spanGaps ? dataset.spanGaps : options.spanGaps,
          tension: custom.tension ? custom.tension : helpers.getValueOrDefault(dataset.lineTension, lineElementOptions.tension),
          backgroundColor: custom.backgroundColor ? custom.backgroundColor : (dataset.backgroundColor || lineElementOptions.backgroundColor),
          borderWidth: custom.borderWidth ? custom.borderWidth : (dataset.borderWidth || lineElementOptions.borderWidth),
          borderColor: custom.borderColor ? custom.borderColor : (dataset.borderColor || lineElementOptions.borderColor),
          borderCapStyle: custom.borderCapStyle ? custom.borderCapStyle : (dataset.borderCapStyle || lineElementOptions.borderCapStyle),
          borderDash: custom.borderDash ? custom.borderDash : (dataset.borderDash || lineElementOptions.borderDash),
          borderDashOffset: custom.borderDashOffset ? custom.borderDashOffset : (dataset.borderDashOffset || lineElementOptions.borderDashOffset),
          borderJoinStyle: custom.borderJoinStyle ? custom.borderJoinStyle : (dataset.borderJoinStyle || lineElementOptions.borderJoinStyle),
          fill: custom.fill ? custom.fill : (dataset.fill !== undefined ? dataset.fill : lineElementOptions.fill),
          steppedLine: custom.steppedLine ? custom.steppedLine : helpers.getValueOrDefault(dataset.steppedLine, lineElementOptions.stepped),
          // Scale
          scaleTop: scale.top,
          scaleBottom: scale.bottom,
          scaleZero: scale.getBasePixel()
        };

        line.pivot();
      }

      // Update Points
      for (i=0, ilen=points.length; i<ilen; ++i) {
        me.updateElement(points[i], i, reset);
      }

      if (showLine && line._model.tension !== 0) {
        me.updateBezierControlPoints();
      }

      // Now pivot the point for animation
      for (i=0, ilen=points.length; i<ilen; ++i) {
        points[i].pivot();
      }
    },

    getPointBackgroundColor: function(point, index) {
      var backgroundColor = this.chart.options.elements.point.backgroundColor;
      var dataset = this.getDataset();
      var custom = point.custom || {};

      if (custom.backgroundColor) {
        backgroundColor = custom.backgroundColor;
      } else if (dataset.pointBackgroundColor) {
        backgroundColor = helpers.getValueAtIndexOrDefault(dataset.pointBackgroundColor, index, backgroundColor);
      } else if (dataset.backgroundColor) {
        backgroundColor = dataset.backgroundColor;
      }

      return backgroundColor;
    },

    getPointBorderColor: function(point, index) {
      var borderColor = this.chart.options.elements.point.borderColor;
      var dataset = this.getDataset();
      var custom = point.custom || {};

      if (custom.borderColor) {
        borderColor = custom.borderColor;
      } else if (dataset.pointBorderColor) {
        borderColor = helpers.getValueAtIndexOrDefault(dataset.pointBorderColor, index, borderColor);
      } else if (dataset.borderColor) {
        borderColor = dataset.borderColor;
      }

      return borderColor;
    },

    getPointBorderWidth: function(point, index) {
      var borderWidth = this.chart.options.elements.point.borderWidth;
      var dataset = this.getDataset();
      var custom = point.custom || {};

      if (custom.borderWidth) {
        borderWidth = custom.borderWidth;
      } else if (dataset.pointBorderWidth) {
        borderWidth = helpers.getValueAtIndexOrDefault(dataset.pointBorderWidth, index, borderWidth);
      } else if (dataset.borderWidth) {
        borderWidth = dataset.borderWidth;
      }

      return borderWidth;
    },

    updateElement: function(point, index, reset) {
      var me = this;
      var meta = me.getMeta();
      var custom = point.custom || {};
      var dataset = me.getDataset();
      var datasetIndex = me.index;
      var value = dataset.data[index];
      var yScale = me.getScaleForId(meta.yAxisID);
      var xScale = me.getScaleForId(meta.xAxisID);
      var pointOptions = me.chart.options.elements.point;
      var x, y;

      // Compatibility: If the properties are defined with only the old name, use those values
      if ((dataset.radius !== undefined) && (dataset.pointRadius === undefined)) {
        dataset.pointRadius = dataset.radius;
      }
      if ((dataset.hitRadius !== undefined) && (dataset.pointHitRadius === undefined)) {
        dataset.pointHitRadius = dataset.hitRadius;
      }

      x = xScale.getPixelForValue(typeof value === 'object' ? value : NaN, index, datasetIndex, me.chart.isCombo);
      y = reset ? yScale.getBasePixel() : me.calculatePointY(value, index, datasetIndex);

      // Utility
      point._xScale = xScale;
      point._yScale = yScale;
      point._datasetIndex = datasetIndex;
      point._index = index;

      // Desired view properties
      point._model = {
        x: x,
        y: y,
        skip: custom.skip || isNaN(x) || isNaN(y),
        // Appearance
        radius: custom.radius || helpers.getValueAtIndexOrDefault(dataset.pointRadius, index, pointOptions.radius),
        pointStyle: custom.pointStyle || helpers.getValueAtIndexOrDefault(dataset.pointStyle, index, pointOptions.pointStyle),
        backgroundColor: me.getPointBackgroundColor(point, index),
        borderColor: me.getPointBorderColor(point, index),
        borderWidth: me.getPointBorderWidth(point, index),
        tension: meta.dataset._model ? meta.dataset._model.tension : 0,
        steppedLine: meta.dataset._model ? meta.dataset._model.steppedLine : false,
        // Tooltip
        hitRadius: custom.hitRadius || helpers.getValueAtIndexOrDefault(dataset.pointHitRadius, index, pointOptions.hitRadius)
      };
    },

    calculatePointY: function(value, index, datasetIndex) {
      var me = this;
      var chart = me.chart;
      var meta = me.getMeta();
      var yScale = me.getScaleForId(meta.yAxisID);
      var sumPos = 0;
      var sumNeg = 0;
      var i, ds, dsMeta;

      if (yScale.options.stacked) {
        for (i = 0; i < datasetIndex; i++) {
          ds = chart.data.datasets[i];
          dsMeta = chart.getDatasetMeta(i);
          if (dsMeta.type === 'line' && chart.isDatasetVisible(i)) {
            var stackedRightValue = Number(yScale.getRightValue(ds.data[index]));
            if (stackedRightValue < 0) {
              sumNeg += stackedRightValue || 0;
            } else {
              sumPos += stackedRightValue || 0;
            }
          }
        }

        var rightValue = Number(yScale.getRightValue(value));
        if (rightValue < 0) {
          return yScale.getPixelForValue(sumNeg + rightValue);
        } else {
          return yScale.getPixelForValue(sumPos + rightValue);
        }
      }

      return yScale.getPixelForValue(value);
    },

    updateBezierControlPoints: function() {
      var me = this;
      var meta = me.getMeta();
      var area = me.chart.chartArea;
      var points = meta.data || [];
      var i, ilen, point, model, controlPoints;

      var needToCap = me.chart.options.elements.line.capBezierPoints;
      function capIfNecessary(pt, min, max) {
        return needToCap ? Math.max(Math.min(pt, max), min) : pt;
      }

      for (i=0, ilen=points.length; i<ilen; ++i) {
        point = points[i];
        model = point._model;
        controlPoints = helpers.splineCurve(
          helpers.previousItem(points, i)._model,
          model,
          helpers.nextItem(points, i)._model,
          meta.dataset._model.tension
        );

        model.controlPointPreviousX = capIfNecessary(controlPoints.previous.x, area.left, area.right);
        model.controlPointPreviousY = capIfNecessary(controlPoints.previous.y, area.top, area.bottom);
        model.controlPointNextX = capIfNecessary(controlPoints.next.x, area.left, area.right);
        model.controlPointNextY = capIfNecessary(controlPoints.next.y, area.top, area.bottom);
      }
    },

    draw: function(ease) {
      var me = this;
      var meta = me.getMeta();
      var points = meta.data || [];
      var easingDecimal = ease || 1;
      var i, ilen;

      // Transition Point Locations
      for (i=0, ilen=points.length; i<ilen; ++i) {
        points[i].transition(easingDecimal);
      }

      // Transition and Draw the line
      if (lineEnabled(me.getDataset(), me.chart.options)) {
        meta.dataset.transition(easingDecimal).draw();
      }

      // Draw the points
      for (i=0, ilen=points.length; i<ilen; ++i) {
        points[i].draw();
      }
    },

    setHoverStyle: function(point) {
      // Point
      var dataset = this.chart.data.datasets[point._datasetIndex];
      var index = point._index;
      var custom = point.custom || {};
      var model = point._model;

      model.radius = custom.hoverRadius || helpers.getValueAtIndexOrDefault(dataset.pointHoverRadius, index, this.chart.options.elements.point.hoverRadius);
      model.backgroundColor = custom.hoverBackgroundColor || helpers.getValueAtIndexOrDefault(dataset.pointHoverBackgroundColor, index, helpers.getHoverColor(model.backgroundColor));
      model.borderColor = custom.hoverBorderColor || helpers.getValueAtIndexOrDefault(dataset.pointHoverBorderColor, index, helpers.getHoverColor(model.borderColor));
      model.borderWidth = custom.hoverBorderWidth || helpers.getValueAtIndexOrDefault(dataset.pointHoverBorderWidth, index, model.borderWidth);
    },

    removeHoverStyle: function(point) {
      var me = this;
      var dataset = me.chart.data.datasets[point._datasetIndex];
      var index = point._index;
      var custom = point.custom || {};
      var model = point._model;

      // Compatibility: If the properties are defined with only the old name, use those values
      if ((dataset.radius !== undefined) && (dataset.pointRadius === undefined)) {
        dataset.pointRadius = dataset.radius;
      }

      model.radius = custom.radius || helpers.getValueAtIndexOrDefault(dataset.pointRadius, index, me.chart.options.elements.point.radius);
      model.backgroundColor = me.getPointBackgroundColor(point, index);
      model.borderColor = me.getPointBorderColor(point, index);
      model.borderWidth = me.getPointBorderWidth(point, index);
    }
  });
};

},{}],19:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.polarArea = {

    scale: {
      type: "radialLinear",
      lineArc: true, // so that lines are circular
      ticks: {
        beginAtZero: true
      }
    },

    //Boolean - Whether to animate the rotation of the chart
    animation: {
      animateRotate: true,
      animateScale: true
    },

    startAngle: -0.5 * Math.PI,
    aspectRatio: 1,
    legendCallback: function(chart) {
      var text = [];
      text.push('<ul class="' + chart.id + '-legend">');

      var data = chart.data;
      var datasets = data.datasets;
      var labels = data.labels;

      if (datasets.length) {
        for (var i = 0; i < datasets[0].data.length; ++i) {
          text.push('<li><span style="background-color:' + datasets[0].backgroundColor[i] + '">');
          if (labels[i]) {
            text.push(labels[i]);
          }
          text.push('</span></li>');
        }
      }

      text.push('</ul>');
      return text.join("");
    },
    legend: {
      labels: {
        generateLabels: function(chart) {
          var data = chart.data;
          if (data.labels.length && data.datasets.length) {
            return data.labels.map(function(label, i) {
              var meta = chart.getDatasetMeta(0);
              var ds = data.datasets[0];
              var arc = meta.data[i];
              var custom = arc.custom || {};
              var getValueAtIndexOrDefault = helpers.getValueAtIndexOrDefault;
              var arcOpts = chart.options.elements.arc;
              var fill = custom.backgroundColor ? custom.backgroundColor : getValueAtIndexOrDefault(ds.backgroundColor, i, arcOpts.backgroundColor);
              var stroke = custom.borderColor ? custom.borderColor : getValueAtIndexOrDefault(ds.borderColor, i, arcOpts.borderColor);
              var bw = custom.borderWidth ? custom.borderWidth : getValueAtIndexOrDefault(ds.borderWidth, i, arcOpts.borderWidth);

              return {
                text: label,
                fillStyle: fill,
                strokeStyle: stroke,
                lineWidth: bw,
                hidden: isNaN(ds.data[i]) || meta.data[i].hidden,

                // Extra data used for toggling the correct item
                index: i
              };
            });
          } else {
            return [];
          }
        }
      },

      onClick: function(e, legendItem) {
        var index = legendItem.index;
        var chart = this.chart;
        var i, ilen, meta;

        for (i = 0, ilen = (chart.data.datasets || []).length; i < ilen; ++i) {
          meta = chart.getDatasetMeta(i);
          meta.data[index].hidden = !meta.data[index].hidden;
        }

        chart.update();
      }
    },

    // Need to override these to give a nice default
    tooltips: {
      callbacks: {
        title: function() {
          return '';
        },
        label: function(tooltipItem, data) {
          return data.labels[tooltipItem.index] + ': ' + tooltipItem.yLabel;
        }
      }
    }
  };

  Chart.controllers.polarArea = Chart.DatasetController.extend({

    dataElementType: Chart.elements.Arc,

    linkScales: helpers.noop,

    update: function(reset) {
      var me = this;
      var chart = me.chart;
      var chartArea = chart.chartArea;
      var meta = me.getMeta();
      var opts = chart.options;
      var arcOpts = opts.elements.arc;
      var minSize = Math.min(chartArea.right - chartArea.left, chartArea.bottom - chartArea.top);
      chart.outerRadius = Math.max((minSize - arcOpts.borderWidth / 2) / 2, 0);
      chart.innerRadius = Math.max(opts.cutoutPercentage ? (chart.outerRadius / 100) * (opts.cutoutPercentage) : 1, 0);
      chart.radiusLength = (chart.outerRadius - chart.innerRadius) / chart.getVisibleDatasetCount();

      me.outerRadius = chart.outerRadius - (chart.radiusLength * me.index);
      me.innerRadius = me.outerRadius - chart.radiusLength;

      meta.count = me.countVisibleElements();

      helpers.each(meta.data, function(arc, index) {
        me.updateElement(arc, index, reset);
      });
    },

    updateElement: function(arc, index, reset) {
      var me = this;
      var chart = me.chart;
      var dataset = me.getDataset();
      var opts = chart.options;
      var animationOpts = opts.animation;
      var scale = chart.scale;
      var getValueAtIndexOrDefault = helpers.getValueAtIndexOrDefault;
      var labels = chart.data.labels;

      var circumference = me.calculateCircumference(dataset.data[index]);
      var centerX = scale.xCenter;
      var centerY = scale.yCenter;

      // If there is NaN data before us, we need to calculate the starting angle correctly.
      // We could be way more efficient here, but its unlikely that the polar area chart will have a lot of data
      var visibleCount = 0;
      var meta = me.getMeta();
      for (var i = 0; i < index; ++i) {
        if (!isNaN(dataset.data[i]) && !meta.data[i].hidden) {
          ++visibleCount;
        }
      }

      //var negHalfPI = -0.5 * Math.PI;
      var datasetStartAngle = opts.startAngle;
      var distance = arc.hidden ? 0 : scale.getDistanceFromCenterForValue(dataset.data[index]);
      var startAngle = datasetStartAngle + (circumference * visibleCount);
      var endAngle = startAngle + (arc.hidden ? 0 : circumference);

      var resetRadius = animationOpts.animateScale ? 0 : scale.getDistanceFromCenterForValue(dataset.data[index]);

      helpers.extend(arc, {
        // Utility
        _datasetIndex: me.index,
        _index: index,
        _scale: scale,

        // Desired view properties
        _model: {
          x: centerX,
          y: centerY,
          innerRadius: 0,
          outerRadius: reset ? resetRadius : distance,
          startAngle: reset && animationOpts.animateRotate ? datasetStartAngle : startAngle,
          endAngle: reset && animationOpts.animateRotate ? datasetStartAngle : endAngle,
          label: getValueAtIndexOrDefault(labels, index, labels[index])
        }
      });

      // Apply border and fill style
      me.removeHoverStyle(arc);

      arc.pivot();
    },

    removeHoverStyle: function(arc) {
      Chart.DatasetController.prototype.removeHoverStyle.call(this, arc, this.chart.options.elements.arc);
    },

    countVisibleElements: function() {
      var dataset = this.getDataset();
      var meta = this.getMeta();
      var count = 0;

      helpers.each(meta.data, function(element, index) {
        if (!isNaN(dataset.data[index]) && !element.hidden) {
          count++;
        }
      });

      return count;
    },

    calculateCircumference: function(value) {
      var count = this.getMeta().count;
      if (count > 0 && !isNaN(value)) {
        return (2 * Math.PI) / count;
      } else {
        return 0;
      }
    }
  });
};

},{}],20:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.radar = {
    scale: {
      type: "radialLinear"
    },
    elements: {
      line: {
        tension: 0 // no bezier in radar
      }
    }
  };

  Chart.controllers.radar = Chart.DatasetController.extend({

    datasetElementType: Chart.elements.Line,

    dataElementType: Chart.elements.Point,

    linkScales: helpers.noop,

    addElementAndReset: function(index) {
      Chart.DatasetController.prototype.addElementAndReset.call(this, index);

      // Make sure bezier control points are updated
      this.updateBezierControlPoints();
    },

    update: function(reset) {
      var me = this;
      var meta = me.getMeta();
      var line = meta.dataset;
      var points = meta.data;
      var custom = line.custom || {};
      var dataset = me.getDataset();
      var lineElementOptions = me.chart.options.elements.line;
      var scale = me.chart.scale;

      // Compatibility: If the properties are defined with only the old name, use those values
      if ((dataset.tension !== undefined) && (dataset.lineTension === undefined)) {
        dataset.lineTension = dataset.tension;
      }

      helpers.extend(meta.dataset, {
        // Utility
        _datasetIndex: me.index,
        // Data
        _children: points,
        _loop: true,
        // Model
        _model: {
          // Appearance
          tension: custom.tension ? custom.tension : helpers.getValueOrDefault(dataset.lineTension, lineElementOptions.tension),
          backgroundColor: custom.backgroundColor ? custom.backgroundColor : (dataset.backgroundColor || lineElementOptions.backgroundColor),
          borderWidth: custom.borderWidth ? custom.borderWidth : (dataset.borderWidth || lineElementOptions.borderWidth),
          borderColor: custom.borderColor ? custom.borderColor : (dataset.borderColor || lineElementOptions.borderColor),
          fill: custom.fill ? custom.fill : (dataset.fill !== undefined ? dataset.fill : lineElementOptions.fill),
          borderCapStyle: custom.borderCapStyle ? custom.borderCapStyle : (dataset.borderCapStyle || lineElementOptions.borderCapStyle),
          borderDash: custom.borderDash ? custom.borderDash : (dataset.borderDash || lineElementOptions.borderDash),
          borderDashOffset: custom.borderDashOffset ? custom.borderDashOffset : (dataset.borderDashOffset || lineElementOptions.borderDashOffset),
          borderJoinStyle: custom.borderJoinStyle ? custom.borderJoinStyle : (dataset.borderJoinStyle || lineElementOptions.borderJoinStyle),

          // Scale
          scaleTop: scale.top,
          scaleBottom: scale.bottom,
          scaleZero: scale.getBasePosition()
        }
      });

      meta.dataset.pivot();

      // Update Points
      helpers.each(points, function(point, index) {
        me.updateElement(point, index, reset);
      }, me);


      // Update bezier control points
      me.updateBezierControlPoints();
    },
    updateElement: function(point, index, reset) {
      var me = this;
      var custom = point.custom || {};
      var dataset = me.getDataset();
      var scale = me.chart.scale;
      var pointElementOptions = me.chart.options.elements.point;
      var pointPosition = scale.getPointPositionForValue(index, dataset.data[index]);

      helpers.extend(point, {
        // Utility
        _datasetIndex: me.index,
        _index: index,
        _scale: scale,

        // Desired view properties
        _model: {
          x: reset ? scale.xCenter : pointPosition.x, // value not used in dataset scale, but we want a consistent API between scales
          y: reset ? scale.yCenter : pointPosition.y,

          // Appearance
          tension: custom.tension ? custom.tension : helpers.getValueOrDefault(dataset.tension, me.chart.options.elements.line.tension),
          radius: custom.radius ? custom.radius : helpers.getValueAtIndexOrDefault(dataset.pointRadius, index, pointElementOptions.radius),
          backgroundColor: custom.backgroundColor ? custom.backgroundColor : helpers.getValueAtIndexOrDefault(dataset.pointBackgroundColor, index, pointElementOptions.backgroundColor),
          borderColor: custom.borderColor ? custom.borderColor : helpers.getValueAtIndexOrDefault(dataset.pointBorderColor, index, pointElementOptions.borderColor),
          borderWidth: custom.borderWidth ? custom.borderWidth : helpers.getValueAtIndexOrDefault(dataset.pointBorderWidth, index, pointElementOptions.borderWidth),
          pointStyle: custom.pointStyle ? custom.pointStyle : helpers.getValueAtIndexOrDefault(dataset.pointStyle, index, pointElementOptions.pointStyle),

          // Tooltip
          hitRadius: custom.hitRadius ? custom.hitRadius : helpers.getValueAtIndexOrDefault(dataset.hitRadius, index, pointElementOptions.hitRadius)
        }
      });

      point._model.skip = custom.skip ? custom.skip : (isNaN(point._model.x) || isNaN(point._model.y));
    },
    updateBezierControlPoints: function() {
      var chartArea = this.chart.chartArea;
      var meta = this.getMeta();

      helpers.each(meta.data, function(point, index) {
        var model = point._model;
        var controlPoints = helpers.splineCurve(
          helpers.previousItem(meta.data, index, true)._model,
          model,
          helpers.nextItem(meta.data, index, true)._model,
          model.tension
        );

        // Prevent the bezier going outside of the bounds of the graph
        model.controlPointPreviousX = Math.max(Math.min(controlPoints.previous.x, chartArea.right), chartArea.left);
        model.controlPointPreviousY = Math.max(Math.min(controlPoints.previous.y, chartArea.bottom), chartArea.top);

        model.controlPointNextX = Math.max(Math.min(controlPoints.next.x, chartArea.right), chartArea.left);
        model.controlPointNextY = Math.max(Math.min(controlPoints.next.y, chartArea.bottom), chartArea.top);

        // Now pivot the point for animation
        point.pivot();
      });
    },

    draw: function(ease) {
      var meta = this.getMeta();
      var easingDecimal = ease || 1;

      // Transition Point Locations
      helpers.each(meta.data, function(point) {
        point.transition(easingDecimal);
      });

      // Transition and Draw the line
      meta.dataset.transition(easingDecimal).draw();

      // Draw the points
      helpers.each(meta.data, function(point) {
        point.draw();
      });
    },

    setHoverStyle: function(point) {
      // Point
      var dataset = this.chart.data.datasets[point._datasetIndex];
      var custom = point.custom || {};
      var index = point._index;
      var model = point._model;

      model.radius = custom.hoverRadius ? custom.hoverRadius : helpers.getValueAtIndexOrDefault(dataset.pointHoverRadius, index, this.chart.options.elements.point.hoverRadius);
      model.backgroundColor = custom.hoverBackgroundColor ? custom.hoverBackgroundColor : helpers.getValueAtIndexOrDefault(dataset.pointHoverBackgroundColor, index, helpers.getHoverColor(model.backgroundColor));
      model.borderColor = custom.hoverBorderColor ? custom.hoverBorderColor : helpers.getValueAtIndexOrDefault(dataset.pointHoverBorderColor, index, helpers.getHoverColor(model.borderColor));
      model.borderWidth = custom.hoverBorderWidth ? custom.hoverBorderWidth : helpers.getValueAtIndexOrDefault(dataset.pointHoverBorderWidth, index, model.borderWidth);
    },

    removeHoverStyle: function(point) {
      var dataset = this.chart.data.datasets[point._datasetIndex];
      var custom = point.custom || {};
      var index = point._index;
      var model = point._model;
      var pointElementOptions = this.chart.options.elements.point;

      model.radius = custom.radius ? custom.radius : helpers.getValueAtIndexOrDefault(dataset.radius, index, pointElementOptions.radius);
      model.backgroundColor = custom.backgroundColor ? custom.backgroundColor : helpers.getValueAtIndexOrDefault(dataset.pointBackgroundColor, index, pointElementOptions.backgroundColor);
      model.borderColor = custom.borderColor ? custom.borderColor : helpers.getValueAtIndexOrDefault(dataset.pointBorderColor, index, pointElementOptions.borderColor);
      model.borderWidth = custom.borderWidth ? custom.borderWidth : helpers.getValueAtIndexOrDefault(dataset.pointBorderWidth, index, pointElementOptions.borderWidth);
    }
  });
};

},{}],21:[function(require,module,exports){
/*global window: false */
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.global.animation = {
    duration: 1000,
    easing: "easeOutQuart",
    onProgress: helpers.noop,
    onComplete: helpers.noop
  };

  Chart.Animation = Chart.Element.extend({
    currentStep: null, // the current animation step
    numSteps: 60, // default number of steps
    easing: "", // the easing to use for this animation
    render: null, // render function used by the animation service

    onAnimationProgress: null, // user specified callback to fire on each step of the animation
    onAnimationComplete: null // user specified callback to fire when the animation finishes
  });

  Chart.animationService = {
    frameDuration: 17,
    animations: [],
    dropFrames: 0,
    request: null,
    addAnimation: function(chartInstance, animationObject, duration, lazy) {
      var me = this;

      if (!lazy) {
        chartInstance.animating = true;
      }

      for (var index = 0; index < me.animations.length; ++index) {
        if (me.animations[index].chartInstance === chartInstance) {
          // replacing an in progress animation
          me.animations[index].animationObject = animationObject;
          return;
        }
      }

      me.animations.push({
        chartInstance: chartInstance,
        animationObject: animationObject
      });

      // If there are no animations queued, manually kickstart a digest, for lack of a better word
      if (me.animations.length === 1) {
        me.requestAnimationFrame();
      }
    },
    // Cancel the animation for a given chart instance
    cancelAnimation: function(chartInstance) {
      var index = helpers.findIndex(this.animations, function(animationWrapper) {
        return animationWrapper.chartInstance === chartInstance;
      });

      if (index !== -1) {
        this.animations.splice(index, 1);
        chartInstance.animating = false;
      }
    },
    requestAnimationFrame: function() {
      var me = this;
      if (me.request === null) {
        // Skip animation frame requests until the active one is executed.
        // This can happen when processing mouse events, e.g. 'mousemove'
        // and 'mouseout' events will trigger multiple renders.
        me.request = helpers.requestAnimFrame.call(window, function() {
          me.request = null;
          me.startDigest();
        });
      }
    },
    startDigest: function() {
      var me = this;

      var startTime = Date.now();
      var framesToDrop = 0;

      if (me.dropFrames > 1) {
        framesToDrop = Math.floor(me.dropFrames);
        me.dropFrames = me.dropFrames % 1;
      }

      var i = 0;
      while (i < me.animations.length) {
        if (me.animations[i].animationObject.currentStep === null) {
          me.animations[i].animationObject.currentStep = 0;
        }

        me.animations[i].animationObject.currentStep += 1 + framesToDrop;

        if (me.animations[i].animationObject.currentStep > me.animations[i].animationObject.numSteps) {
          me.animations[i].animationObject.currentStep = me.animations[i].animationObject.numSteps;
        }

        me.animations[i].animationObject.render(me.animations[i].chartInstance, me.animations[i].animationObject);
        if (me.animations[i].animationObject.onAnimationProgress && me.animations[i].animationObject.onAnimationProgress.call) {
          me.animations[i].animationObject.onAnimationProgress.call(me.animations[i].chartInstance, me.animations[i]);
        }

        if (me.animations[i].animationObject.currentStep === me.animations[i].animationObject.numSteps) {
          if (me.animations[i].animationObject.onAnimationComplete && me.animations[i].animationObject.onAnimationComplete.call) {
            me.animations[i].animationObject.onAnimationComplete.call(me.animations[i].chartInstance, me.animations[i]);
          }

          // executed the last frame. Remove the animation.
          me.animations[i].chartInstance.animating = false;

          me.animations.splice(i, 1);
        } else {
          ++i;
        }
      }

      var endTime = Date.now();
      var dropFrames = (endTime - startTime) / me.frameDuration;

      me.dropFrames += dropFrames;

      // Do we have more stuff to animate?
      if (me.animations.length > 0) {
        me.requestAnimationFrame();
      }
    }
  };
};
},{}],22:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {
  // Global Chart canvas helpers object for drawing items to canvas
  var helpers = Chart.canvasHelpers = {};

  helpers.drawPoint = function(ctx, pointStyle, radius, x, y) {
    var type, edgeLength, xOffset, yOffset, height, size;

    if (typeof pointStyle === 'object') {
      type = pointStyle.toString();
      if (type === '[object HTMLImageElement]' || type === '[object HTMLCanvasElement]') {
        ctx.drawImage(pointStyle, x - pointStyle.width / 2, y - pointStyle.height / 2);
        return;
      }
    }

    if (isNaN(radius) || radius <= 0) {
      return;
    }

    switch (pointStyle) {
    // Default includes circle
    default:
      ctx.beginPath();
      ctx.arc(x, y, radius, 0, Math.PI * 2);
      ctx.closePath();
      ctx.fill();
      break;
    case 'triangle':
      ctx.beginPath();
      edgeLength = 3 * radius / Math.sqrt(3);
      height = edgeLength * Math.sqrt(3) / 2;
      ctx.moveTo(x - edgeLength / 2, y + height / 3);
      ctx.lineTo(x + edgeLength / 2, y + height / 3);
      ctx.lineTo(x, y - 2 * height / 3);
      ctx.closePath();
      ctx.fill();
      break;
    case 'rect':
      size = 1 / Math.SQRT2 * radius;
      ctx.beginPath();
      ctx.fillRect(x - size, y - size, 2 * size,  2 * size);
      ctx.strokeRect(x - size, y - size, 2 * size, 2 * size);
      break;
    case 'rectRot':
      size = 1 / Math.SQRT2 * radius;
      ctx.beginPath();
      ctx.moveTo(x - size, y);
      ctx.lineTo(x, y + size);
      ctx.lineTo(x + size, y);
      ctx.lineTo(x, y - size);
      ctx.closePath();
      ctx.fill();
      break;
    case 'cross':
      ctx.beginPath();
      ctx.moveTo(x, y + radius);
      ctx.lineTo(x, y - radius);
      ctx.moveTo(x - radius, y);
      ctx.lineTo(x + radius, y);
      ctx.closePath();
      break;
    case 'crossRot':
      ctx.beginPath();
      xOffset = Math.cos(Math.PI / 4) * radius;
      yOffset = Math.sin(Math.PI / 4) * radius;
      ctx.moveTo(x - xOffset, y - yOffset);
      ctx.lineTo(x + xOffset, y + yOffset);
      ctx.moveTo(x - xOffset, y + yOffset);
      ctx.lineTo(x + xOffset, y - yOffset);
      ctx.closePath();
      break;
    case 'star':
      ctx.beginPath();
      ctx.moveTo(x, y + radius);
      ctx.lineTo(x, y - radius);
      ctx.moveTo(x - radius, y);
      ctx.lineTo(x + radius, y);
      xOffset = Math.cos(Math.PI / 4) * radius;
      yOffset = Math.sin(Math.PI / 4) * radius;
      ctx.moveTo(x - xOffset, y - yOffset);
      ctx.lineTo(x + xOffset, y + yOffset);
      ctx.moveTo(x - xOffset, y + yOffset);
      ctx.lineTo(x + xOffset, y - yOffset);
      ctx.closePath();
      break;
    case 'line':
      ctx.beginPath();
      ctx.moveTo(x - radius, y);
      ctx.lineTo(x + radius, y);
      ctx.closePath();
      break;
    case 'dash':
      ctx.beginPath();
      ctx.moveTo(x, y);
      ctx.lineTo(x + radius, y);
      ctx.closePath();
      break;
    }

    ctx.stroke();
  };
};
},{}],23:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;
  //Create a dictionary of chart types, to allow for extension of existing types
  Chart.types = {};

  //Store a reference to each instance - allowing us to globally resize chart instances on window resize.
  //Destroy method on the chart will remove the instance of the chart from this reference.
  Chart.instances = {};

  // Controllers available for dataset visualization eg. bar, line, slice, etc.
  Chart.controllers = {};

  /**
   * @class Chart.Controller
   * The main controller of a chart.
   */
  Chart.Controller = function(instance) {

    this.chart = instance;
    this.config = instance.config;
    this.options = this.config.options = helpers.configMerge(Chart.defaults.global, Chart.defaults[this.config.type], this.config.options || {});
    this.id = helpers.uid();

    Object.defineProperty(this, 'data', {
      get: function() {
        return this.config.data;
      }
    });

    //Add the chart instance to the global namespace
    Chart.instances[this.id] = this;

    if (this.options.responsive) {
      // Silent resize before chart draws
      this.resize(true);
    }

    this.initialize();

    return this;
  };

  helpers.extend(Chart.Controller.prototype, /** @lends Chart.Controller */ {

    initialize: function() {
      var me = this;
      // Before init plugin notification
      Chart.plugins.notify('beforeInit', [me]);

      me.bindEvents();

      // Make sure controllers are built first so that each dataset is bound to an axis before the scales
      // are built
      me.ensureScalesHaveIDs();
      me.buildOrUpdateControllers();
      me.buildScales();
      me.updateLayout();
      me.resetElements();
      me.initToolTip();
      me.update();

      // After init plugin notification
      Chart.plugins.notify('afterInit', [me]);

      return me;
    },

    clear: function() {
      helpers.clear(this.chart);
      return this;
    },

    stop: function() {
      // Stops any current animation loop occuring
      Chart.animationService.cancelAnimation(this);
      return this;
    },

    resize: function resize(silent) {
      var me = this;
      var chart = me.chart;
      var canvas = chart.canvas;
      var newWidth = helpers.getMaximumWidth(canvas);
      var aspectRatio = chart.aspectRatio;
      var newHeight = (me.options.maintainAspectRatio && isNaN(aspectRatio) === false && isFinite(aspectRatio) && aspectRatio !== 0) ? newWidth / aspectRatio : helpers.getMaximumHeight(canvas);

      var sizeChanged = chart.width !== newWidth || chart.height !== newHeight;

      if (!sizeChanged) {
        return me;
      }

      canvas.width = chart.width = newWidth;
      canvas.height = chart.height = newHeight;

      helpers.retinaScale(chart);

      // Notify any plugins about the resize
      var newSize = { width: newWidth, height: newHeight };
      Chart.plugins.notify('resize', [me, newSize]);

      // Notify of resize
      if (me.options.onResize) {
        me.options.onResize(me, newSize);
      }

      if (!silent) {
        me.stop();
        me.update(me.options.responsiveAnimationDuration);
      }

      return me;
    },

    ensureScalesHaveIDs: function() {
      var options = this.options;
      var scalesOptions = options.scales || {};
      var scaleOptions = options.scale;

      helpers.each(scalesOptions.xAxes, function(xAxisOptions, index) {
        xAxisOptions.id = xAxisOptions.id || ('x-axis-' + index);
      });

      helpers.each(scalesOptions.yAxes, function(yAxisOptions, index) {
        yAxisOptions.id = yAxisOptions.id || ('y-axis-' + index);
      });

      if (scaleOptions) {
        scaleOptions.id = scaleOptions.id || 'scale';
      }
    },

    /**
     * Builds a map of scale ID to scale object for future lookup.
     */
    buildScales: function() {
      var me = this;
      var options = me.options;
      var scales = me.scales = {};
      var items = [];

      if (options.scales) {
        items = items.concat(
          (options.scales.xAxes || []).map(function(xAxisOptions) {
            return { options: xAxisOptions, dtype: 'category' }; }),
          (options.scales.yAxes || []).map(function(yAxisOptions) {
            return { options: yAxisOptions, dtype: 'linear' }; }));
      }

      if (options.scale) {
        items.push({ options: options.scale, dtype: 'radialLinear', isDefault: true });
      }

      helpers.each(items, function(item) {
        var scaleOptions = item.options;
        var scaleType = helpers.getValueOrDefault(scaleOptions.type, item.dtype);
        var scaleClass = Chart.scaleService.getScaleConstructor(scaleType);
        if (!scaleClass) {
          return;
        }

        var scale = new scaleClass({
          id: scaleOptions.id,
          options: scaleOptions,
          ctx: me.chart.ctx,
          chart: me
        });

        scales[scale.id] = scale;

        // TODO(SB): I think we should be able to remove this custom case (options.scale)
        // and consider it as a regular scale part of the "scales"" map only! This would
        // make the logic easier and remove some useless? custom code.
        if (item.isDefault) {
          me.scale = scale;
        }
      });

      Chart.scaleService.addScalesToLayout(this);
    },

    updateLayout: function() {
      Chart.layoutService.update(this, this.chart.width, this.chart.height);
    },

    buildOrUpdateControllers: function() {
      var me = this;
      var types = [];
      var newControllers = [];

      helpers.each(me.data.datasets, function(dataset, datasetIndex) {
        var meta = me.getDatasetMeta(datasetIndex);
        if (!meta.type) {
          meta.type = dataset.type || me.config.type;
        }

        types.push(meta.type);

        if (meta.controller) {
          meta.controller.updateIndex(datasetIndex);
        } else {
          meta.controller = new Chart.controllers[meta.type](me, datasetIndex);
          newControllers.push(meta.controller);
        }
      }, me);

      if (types.length > 1) {
        for (var i = 1; i < types.length; i++) {
          if (types[i] !== types[i - 1]) {
            me.isCombo = true;
            break;
          }
        }
      }

      return newControllers;
    },

    resetElements: function() {
      var me = this;
      helpers.each(me.data.datasets, function(dataset, datasetIndex) {
        me.getDatasetMeta(datasetIndex).controller.reset();
      }, me);
    },

    update: function update(animationDuration, lazy) {
      var me = this;
      Chart.plugins.notify('beforeUpdate', [me]);

      // In case the entire data object changed
      me.tooltip._data = me.data;

      // Make sure dataset controllers are updated and new controllers are reset
      var newControllers = me.buildOrUpdateControllers();

      // Make sure all dataset controllers have correct meta data counts
      helpers.each(me.data.datasets, function(dataset, datasetIndex) {
        me.getDatasetMeta(datasetIndex).controller.buildOrUpdateElements();
      }, me);

      Chart.layoutService.update(me, me.chart.width, me.chart.height);

      // Apply changes to the dataets that require the scales to have been calculated i.e BorderColor chages
      Chart.plugins.notify('afterScaleUpdate', [me]);

      // Can only reset the new controllers after the scales have been updated
      helpers.each(newControllers, function(controller) {
        controller.reset();
      });

      me.updateDatasets();

      // Do this before render so that any plugins that need final scale updates can use it
      Chart.plugins.notify('afterUpdate', [me]);

      me.render(animationDuration, lazy);
    },

    /**
     * @method beforeDatasetsUpdate
     * @description Called before all datasets are updated. If a plugin returns false,
     * the datasets update will be cancelled until another chart update is triggered.
     * @param {Object} instance the chart instance being updated.
     * @returns {Boolean} false to cancel the datasets update.
     * @memberof Chart.PluginBase
     * @since version 2.1.5
     * @instance
     */

    /**
     * @method afterDatasetsUpdate
     * @description Called after all datasets have been updated. Note that this
     * extension will not be called if the datasets update has been cancelled.
     * @param {Object} instance the chart instance being updated.
     * @memberof Chart.PluginBase
     * @since version 2.1.5
     * @instance
     */

    /**
     * Updates all datasets unless a plugin returns false to the beforeDatasetsUpdate
     * extension, in which case no datasets will be updated and the afterDatasetsUpdate
     * notification will be skipped.
     * @protected
     * @instance
     */
    updateDatasets: function() {
      var me = this;
      var i, ilen;

      if (Chart.plugins.notify('beforeDatasetsUpdate', [ me ])) {
        for (i = 0, ilen = me.data.datasets.length; i < ilen; ++i) {
          me.getDatasetMeta(i).controller.update();
        }

        Chart.plugins.notify('afterDatasetsUpdate', [ me ]);
      }
    },

    render: function render(duration, lazy) {
      var me = this;
      Chart.plugins.notify('beforeRender', [me]);

      var animationOptions = me.options.animation;
      if (animationOptions && ((typeof duration !== 'undefined' && duration !== 0) || (typeof duration === 'undefined' && animationOptions.duration !== 0))) {
        var animation = new Chart.Animation();
        animation.numSteps = (duration || animationOptions.duration) / 16.66; //60 fps
        animation.easing = animationOptions.easing;

        // render function
        animation.render = function(chartInstance, animationObject) {
          var easingFunction = helpers.easingEffects[animationObject.easing];
          var stepDecimal = animationObject.currentStep / animationObject.numSteps;
          var easeDecimal = easingFunction(stepDecimal);

          chartInstance.draw(easeDecimal, stepDecimal, animationObject.currentStep);
        };

        // user events
        animation.onAnimationProgress = animationOptions.onProgress;
        animation.onAnimationComplete = animationOptions.onComplete;

        Chart.animationService.addAnimation(me, animation, duration, lazy);
      } else {
        me.draw();
        if (animationOptions && animationOptions.onComplete && animationOptions.onComplete.call) {
          animationOptions.onComplete.call(me);
        }
      }
      return me;
    },

    draw: function(ease) {
      var me = this;
      var easingDecimal = ease || 1;
      me.clear();

      Chart.plugins.notify('beforeDraw', [me, easingDecimal]);

      // Draw all the scales
      helpers.each(me.boxes, function(box) {
        box.draw(me.chartArea);
      }, me);
      if (me.scale) {
        me.scale.draw();
      }

      Chart.plugins.notify('beforeDatasetsDraw', [me, easingDecimal]);

      // Draw each dataset via its respective controller (reversed to support proper line stacking)
      helpers.each(me.data.datasets, function(dataset, datasetIndex) {
        if (me.isDatasetVisible(datasetIndex)) {
          me.getDatasetMeta(datasetIndex).controller.draw(ease);
        }
      }, me, true);

      Chart.plugins.notify('afterDatasetsDraw', [me, easingDecimal]);

      // Finally draw the tooltip
      me.tooltip.transition(easingDecimal).draw();

      Chart.plugins.notify('afterDraw', [me, easingDecimal]);
    },

    // Get the single element that was clicked on
    // @return : An object containing the dataset index and element index of the matching element. Also contains the rectangle that was draw
    getElementAtEvent: function(e) {
      var me = this;
      var eventPosition = helpers.getRelativePosition(e, me.chart);
      var elementsArray = [];

      helpers.each(me.data.datasets, function(dataset, datasetIndex) {
        if (me.isDatasetVisible(datasetIndex)) {
          var meta = me.getDatasetMeta(datasetIndex);
          helpers.each(meta.data, function(element) {
            if (element.inRange(eventPosition.x, eventPosition.y)) {
              elementsArray.push(element);
              return elementsArray;
            }
          });
        }
      });

      return elementsArray;
    },

    getElementsAtEvent: function(e) {
      var me = this;
      var eventPosition = helpers.getRelativePosition(e, me.chart);
      var elementsArray = [];

      var found = (function() {
        if (me.data.datasets) {
          for (var i = 0; i < me.data.datasets.length; i++) {
            var meta = me.getDatasetMeta(i);
            if (me.isDatasetVisible(i)) {
              for (var j = 0; j < meta.data.length; j++) {
                if (meta.data[j].inRange(eventPosition.x, eventPosition.y)) {
                  return meta.data[j];
                }
              }
            }
          }
        }
      }).call(me);

      if (!found) {
        return elementsArray;
      }

      helpers.each(me.data.datasets, function(dataset, datasetIndex) {
        if (me.isDatasetVisible(datasetIndex)) {
          var meta = me.getDatasetMeta(datasetIndex),
            element = meta.data[found._index];
          if(element && !element._view.skip){
            elementsArray.push(element);
          }
        }
      }, me);

      return elementsArray;
    },

        getElementsAtXAxis: function(e){
            var me = this;
            var eventPosition = helpers.getRelativePosition(e, me.chart);
            var elementsArray = [];

            var found = (function() {
                if (me.data.datasets) {
                    for (var i = 0; i < me.data.datasets.length; i++) {
                        var meta = me.getDatasetMeta(i);
                        if (me.isDatasetVisible(i)) {
                            for (var j = 0; j < meta.data.length; j++) {
                                if (meta.data[j].inLabelRange(eventPosition.x, eventPosition.y)) {
                                    return meta.data[j];
                                }
                            }
                        }
                    }
                }
            }).call(me);

            if (!found) {
                return elementsArray;
            }

            helpers.each(me.data.datasets, function(dataset, datasetIndex) {
                if (me.isDatasetVisible(datasetIndex)) {
                    var meta = me.getDatasetMeta(datasetIndex);
                    if(!meta.data[found._index]._view.skip){
                      elementsArray.push(meta.data[found._index]);
                    }
                }
            }, me);

            return elementsArray;
        },

    getElementsAtEventForMode: function(e, mode) {
      var me = this;
      switch (mode) {
      case 'single':
        return me.getElementAtEvent(e);
      case 'label':
        return me.getElementsAtEvent(e);
      case 'dataset':
        return me.getDatasetAtEvent(e);
            case 'x-axis':
                return me.getElementsAtXAxis(e);
      default:
        return e;
      }
    },

    getDatasetAtEvent: function(e) {
      var elementsArray = this.getElementAtEvent(e);

      if (elementsArray.length > 0) {
        elementsArray = this.getDatasetMeta(elementsArray[0]._datasetIndex).data;
      }

      return elementsArray;
    },

    getDatasetMeta: function(datasetIndex) {
      var me = this;
      var dataset = me.data.datasets[datasetIndex];
      if (!dataset._meta) {
        dataset._meta = {};
      }

      var meta = dataset._meta[me.id];
      if (!meta) {
        meta = dataset._meta[me.id] = {
        type: null,
        data: [],
        dataset: null,
        controller: null,
        hidden: null,     // See isDatasetVisible() comment
        xAxisID: null,
        yAxisID: null
      };
      }

      return meta;
    },

    getVisibleDatasetCount: function() {
      var count = 0;
      for (var i = 0, ilen = this.data.datasets.length; i<ilen; ++i) {
         if (this.isDatasetVisible(i)) {
          count++;
        }
      }
      return count;
    },

    isDatasetVisible: function(datasetIndex) {
      var meta = this.getDatasetMeta(datasetIndex);

      // meta.hidden is a per chart dataset hidden flag override with 3 states: if true or false,
      // the dataset.hidden value is ignored, else if null, the dataset hidden state is returned.
      return typeof meta.hidden === 'boolean'? !meta.hidden : !this.data.datasets[datasetIndex].hidden;
    },

    generateLegend: function() {
      return this.options.legendCallback(this);
    },

    destroy: function() {
      var me = this;
      me.stop();
      me.clear();
      helpers.unbindEvents(me, me.events);
      helpers.removeResizeListener(me.chart.canvas.parentNode);

      // Reset canvas height/width attributes
      var canvas = me.chart.canvas;
      canvas.width = me.chart.width;
      canvas.height = me.chart.height;

      // if we scaled the canvas in response to a devicePixelRatio !== 1, we need to undo that transform here
      if (me.chart.originalDevicePixelRatio !== undefined) {
        me.chart.ctx.scale(1 / me.chart.originalDevicePixelRatio, 1 / me.chart.originalDevicePixelRatio);
      }

      // Reset to the old style since it may have been changed by the device pixel ratio changes
      canvas.style.width = me.chart.originalCanvasStyleWidth;
      canvas.style.height = me.chart.originalCanvasStyleHeight;

      Chart.plugins.notify('destroy', [me]);

      delete Chart.instances[me.id];
    },

    toBase64Image: function() {
      return this.chart.canvas.toDataURL.apply(this.chart.canvas, arguments);
    },

    initToolTip: function() {
      var me = this;
      me.tooltip = new Chart.Tooltip({
        _chart: me.chart,
        _chartInstance: me,
        _data: me.data,
        _options: me.options.tooltips
      }, me);
    },

    bindEvents: function() {
      var me = this;
      helpers.bindEvents(me, me.options.events, function(evt) {
        me.eventHandler(evt);
      });
    },

    updateHoverStyle: function(elements, mode, enabled) {
      var method = enabled? 'setHoverStyle' : 'removeHoverStyle';
      var element, i, ilen;

      switch (mode) {
      case 'single':
        elements = [ elements[0] ];
        break;
      case 'label':
      case 'dataset':
            case 'x-axis':
        // elements = elements;
        break;
      default:
        // unsupported mode
        return;
      }

      for (i=0, ilen=elements.length; i<ilen; ++i) {
        element = elements[i];
        if (element) {
          this.getDatasetMeta(element._datasetIndex).controller[method](element);
        }
      }
    },

    eventHandler: function eventHandler(e) {
      var me = this;
      var tooltip = me.tooltip;
      var options = me.options || {};
      var hoverOptions = options.hover;
      var tooltipsOptions = options.tooltips;

      me.lastActive = me.lastActive || [];
      me.lastTooltipActive = me.lastTooltipActive || [];

      // Find Active Elements for hover and tooltips
      if (e.type === 'mouseout') {
        me.active = [];
        me.tooltipActive = [];
      } else {
        me.active = me.getElementsAtEventForMode(e, hoverOptions.mode);
        me.tooltipActive =  me.getElementsAtEventForMode(e, tooltipsOptions.mode);
      }

      // On Hover hook
      if (hoverOptions.onHover) {
        hoverOptions.onHover.call(me, me.active);
      }

      if (e.type === 'mouseup' || e.type === 'click') {
        if (options.onClick) {
          options.onClick.call(me, e, me.active);
        }
        if (me.legend && me.legend.handleEvent) {
          me.legend.handleEvent(e);
        }
      }

      // Remove styling for last active (even if it may still be active)
      if (me.lastActive.length) {
        me.updateHoverStyle(me.lastActive, hoverOptions.mode, false);
      }

      // Built in hover styling
      if (me.active.length && hoverOptions.mode) {
        me.updateHoverStyle(me.active, hoverOptions.mode, true);
      }

      // Built in Tooltips
      if (tooltipsOptions.enabled || tooltipsOptions.custom) {
        tooltip.initialize();
        tooltip._active = me.tooltipActive;
        tooltip.update(true);
      }

      // Hover animations
      tooltip.pivot();

      if (!me.animating) {
        // If entering, leaving, or changing elements, animate the change via pivot
        if (!helpers.arrayEquals(me.active, me.lastActive) ||
          !helpers.arrayEquals(me.tooltipActive, me.lastTooltipActive)) {

          me.stop();

          if (tooltipsOptions.enabled || tooltipsOptions.custom) {
            tooltip.update(true);
          }

          // We only need to render at this point. Updating will cause scales to be
          // recomputed generating flicker & using more memory than necessary.
          me.render(hoverOptions.animationDuration, true);
        }
      }

      // Remember Last Actives
      me.lastActive = me.active;
      me.lastTooltipActive = me.tooltipActive;
      return me;
    }
  });
};

},{}],24:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;
  var noop = helpers.noop;

  // Base class for all dataset controllers (line, bar, etc)
  Chart.DatasetController = function(chart, datasetIndex) {
    this.initialize.call(this, chart, datasetIndex);
  };

  helpers.extend(Chart.DatasetController.prototype, {

    /**
     * Element type used to generate a meta dataset (e.g. Chart.element.Line).
     * @type {Chart.core.element}
     */
    datasetElementType: null,

    /**
     * Element type used to generate a meta data (e.g. Chart.element.Point).
     * @type {Chart.core.element}
     */
    dataElementType: null,

    initialize: function(chart, datasetIndex) {
      var me = this;
      me.chart = chart;
      me.index = datasetIndex;
      me.linkScales();
      me.addElements();
    },

    updateIndex: function(datasetIndex) {
      this.index = datasetIndex;
    },

    linkScales: function() {
      var me = this;
      var meta = me.getMeta();
      var dataset = me.getDataset();

      if (meta.xAxisID === null) {
        meta.xAxisID = dataset.xAxisID || me.chart.options.scales.xAxes[0].id;
      }
      if (meta.yAxisID === null) {
        meta.yAxisID = dataset.yAxisID || me.chart.options.scales.yAxes[0].id;
      }
    },

    getDataset: function() {
      return this.chart.data.datasets[this.index];
    },

    getMeta: function() {
      return this.chart.getDatasetMeta(this.index);
    },

    getScaleForId: function(scaleID) {
      return this.chart.scales[scaleID];
    },

    reset: function() {
      this.update(true);
    },

    createMetaDataset: function() {
      var me = this;
      var type = me.datasetElementType;
      return type && new type({
        _chart: me.chart.chart,
        _datasetIndex: me.index
      });
    },

    createMetaData: function(index) {
      var me = this;
      var type = me.dataElementType;
      return type && new type({
        _chart: me.chart.chart,
        _datasetIndex: me.index,
        _index: index
      });
    },

    addElements: function() {
      var me = this;
      var meta = me.getMeta();
      var data = me.getDataset().data || [];
      var metaData = meta.data;
      var i, ilen;

      for (i=0, ilen=data.length; i<ilen; ++i) {
        metaData[i] = metaData[i] || me.createMetaData(meta, i);
      }

      meta.dataset = meta.dataset || me.createMetaDataset();
    },

    addElementAndReset: function(index) {
      var me = this;
      var element = me.createMetaData(index);
      me.getMeta().data.splice(index, 0, element);
      me.updateElement(element, index, true);
    },

    buildOrUpdateElements: function() {
      // Handle the number of data points changing
      var meta = this.getMeta(),
        md = meta.data,
        numData = this.getDataset().data.length,
        numMetaData = md.length;

      // Make sure that we handle number of datapoints changing
      if (numData < numMetaData) {
        // Remove excess bars for data points that have been removed
        md.splice(numData, numMetaData - numData);
      } else if (numData > numMetaData) {
        // Add new elements
        for (var index = numMetaData; index < numData; ++index) {
          this.addElementAndReset(index);
        }
      }
    },

    update: noop,

    draw: function(ease) {
      var easingDecimal = ease || 1;
      helpers.each(this.getMeta().data, function(element) {
        element.transition(easingDecimal).draw();
      });
    },

    removeHoverStyle: function(element, elementOpts) {
      var dataset = this.chart.data.datasets[element._datasetIndex],
        index = element._index,
        custom = element.custom || {},
        valueOrDefault = helpers.getValueAtIndexOrDefault,
        model = element._model;

      model.backgroundColor = custom.backgroundColor ? custom.backgroundColor : valueOrDefault(dataset.backgroundColor, index, elementOpts.backgroundColor);
      model.borderColor = custom.borderColor ? custom.borderColor : valueOrDefault(dataset.borderColor, index, elementOpts.borderColor);
      model.borderWidth = custom.borderWidth ? custom.borderWidth : valueOrDefault(dataset.borderWidth, index, elementOpts.borderWidth);
    },

    setHoverStyle: function(element) {
      var dataset = this.chart.data.datasets[element._datasetIndex],
        index = element._index,
        custom = element.custom || {},
        valueOrDefault = helpers.getValueAtIndexOrDefault,
        getHoverColor = helpers.getHoverColor,
        model = element._model;

      model.backgroundColor = custom.hoverBackgroundColor ? custom.hoverBackgroundColor : valueOrDefault(dataset.hoverBackgroundColor, index, getHoverColor(model.backgroundColor));
      model.borderColor = custom.hoverBorderColor ? custom.hoverBorderColor : valueOrDefault(dataset.hoverBorderColor, index, getHoverColor(model.borderColor));
      model.borderWidth = custom.hoverBorderWidth ? custom.hoverBorderWidth : valueOrDefault(dataset.hoverBorderWidth, index, model.borderWidth);
    }

    });


  Chart.DatasetController.extend = helpers.inherits;
};
},{}],25:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.elements = {};

  Chart.Element = function(configuration) {
    helpers.extend(this, configuration);
    this.initialize.apply(this, arguments);
  };

  helpers.extend(Chart.Element.prototype, {

    initialize: function() {
      this.hidden = false;
    },

    pivot: function() {
      var me = this;
      if (!me._view) {
        me._view = helpers.clone(me._model);
      }
      me._start = helpers.clone(me._view);
      return me;
    },

    transition: function(ease) {
      var me = this;

      if (!me._view) {
        me._view = helpers.clone(me._model);
      }

      // No animation -> No Transition
      if (ease === 1) {
        me._view = me._model;
        me._start = null;
        return me;
      }

      if (!me._start) {
        me.pivot();
      }

      helpers.each(me._model, function(value, key) {

        if (key[0] === '_') {
          // Only non-underscored properties
        }

        // Init if doesn't exist
        else if (!me._view.hasOwnProperty(key)) {
          if (typeof value === 'number' && !isNaN(me._view[key])) {
            me._view[key] = value * ease;
          } else {
            me._view[key] = value;
          }
        }

        // No unnecessary computations
        else if (value === me._view[key]) {
          // It's the same! Woohoo!
        }

        // Color transitions if possible
        else if (typeof value === 'string') {
          try {
            var color = helpers.color(me._model[key]).mix(helpers.color(me._start[key]), ease);
            me._view[key] = color.rgbString();
          } catch (err) {
            me._view[key] = value;
          }
        }
        // Number transitions
        else if (typeof value === 'number') {
          var startVal = me._start[key] !== undefined && isNaN(me._start[key]) === false ? me._start[key] : 0;
          me._view[key] = ((me._model[key] - startVal) * ease) + startVal;
        }
        // Everything else
        else {
          me._view[key] = value;
        }
      }, me);

      return me;
    },

    tooltipPosition: function() {
      return {
        x: this._model.x,
        y: this._model.y
      };
    },

    hasValue: function() {
      return helpers.isNumber(this._model.x) && helpers.isNumber(this._model.y);
    }
  });

  Chart.Element.extend = helpers.inherits;

};

},{}],26:[function(require,module,exports){
/*global window: false */
/*global document: false */
"use strict";

var color = require(3);

module.exports = function(Chart) {
  //Global Chart helpers object for utility methods and classes
  var helpers = Chart.helpers = {};

  //-- Basic js utility methods
  helpers.each = function(loopable, callback, self, reverse) {
    // Check to see if null or undefined firstly.
    var i, len;
    if (helpers.isArray(loopable)) {
      len = loopable.length;
      if (reverse) {
        for (i = len - 1; i >= 0; i--) {
          callback.call(self, loopable[i], i);
        }
      } else {
        for (i = 0; i < len; i++) {
          callback.call(self, loopable[i], i);
        }
      }
    } else if (typeof loopable === 'object') {
      var keys = Object.keys(loopable);
      len = keys.length;
      for (i = 0; i < len; i++) {
        callback.call(self, loopable[keys[i]], keys[i]);
      }
    }
  };
  helpers.clone = function(obj) {
    var objClone = {};
    helpers.each(obj, function(value, key) {
      if (helpers.isArray(value)) {
        objClone[key] = value.slice(0);
      } else if (typeof value === 'object' && value !== null) {
        objClone[key] = helpers.clone(value);
      } else {
        objClone[key] = value;
      }
    });
    return objClone;
  };
  helpers.extend = function(base) {
    var setFn = function(value, key) { base[key] = value; };
    for (var i = 1, ilen = arguments.length; i < ilen; i++) {
      helpers.each(arguments[i], setFn);
    }
    return base;
  };
  // Need a special merge function to chart configs since they are now grouped
  helpers.configMerge = function(_base) {
    var base = helpers.clone(_base);
    helpers.each(Array.prototype.slice.call(arguments, 1), function(extension) {
      helpers.each(extension, function(value, key) {
        if (key === 'scales') {
          // Scale config merging is complex. Add out own function here for that
          base[key] = helpers.scaleMerge(base.hasOwnProperty(key) ? base[key] : {}, value);

        } else if (key === 'scale') {
          // Used in polar area & radar charts since there is only one scale
          base[key] = helpers.configMerge(base.hasOwnProperty(key) ? base[key] : {}, Chart.scaleService.getScaleDefaults(value.type), value);
        } else if (base.hasOwnProperty(key) && helpers.isArray(base[key]) && helpers.isArray(value)) {
          // In this case we have an array of objects replacing another array. Rather than doing a strict replace,
          // merge. This allows easy scale option merging
          var baseArray = base[key];

          helpers.each(value, function(valueObj, index) {

            if (index < baseArray.length) {
              if (typeof baseArray[index] === 'object' && baseArray[index] !== null && typeof valueObj === 'object' && valueObj !== null) {
                // Two objects are coming together. Do a merge of them.
                baseArray[index] = helpers.configMerge(baseArray[index], valueObj);
              } else {
                // Just overwrite in this case since there is nothing to merge
                baseArray[index] = valueObj;
              }
            } else {
              baseArray.push(valueObj); // nothing to merge
            }
          });

        } else if (base.hasOwnProperty(key) && typeof base[key] === "object" && base[key] !== null && typeof value === "object") {
          // If we are overwriting an object with an object, do a merge of the properties.
          base[key] = helpers.configMerge(base[key], value);

        } else {
          // can just overwrite the value in this case
          base[key] = value;
        }
      });
    });

    return base;
  };
  helpers.scaleMerge = function(_base, extension) {
    var base = helpers.clone(_base);

    helpers.each(extension, function(value, key) {
      if (key === 'xAxes' || key === 'yAxes') {
        // These properties are arrays of items
        if (base.hasOwnProperty(key)) {
          helpers.each(value, function(valueObj, index) {
            var axisType = helpers.getValueOrDefault(valueObj.type, key === 'xAxes' ? 'category' : 'linear');
            var axisDefaults = Chart.scaleService.getScaleDefaults(axisType);
            if (index >= base[key].length || !base[key][index].type) {
              base[key].push(helpers.configMerge(axisDefaults, valueObj));
            } else if (valueObj.type && valueObj.type !== base[key][index].type) {
              // Type changed. Bring in the new defaults before we bring in valueObj so that valueObj can override the correct scale defaults
              base[key][index] = helpers.configMerge(base[key][index], axisDefaults, valueObj);
            } else {
              // Type is the same
              base[key][index] = helpers.configMerge(base[key][index], valueObj);
            }
          });
        } else {
          base[key] = [];
          helpers.each(value, function(valueObj) {
            var axisType = helpers.getValueOrDefault(valueObj.type, key === 'xAxes' ? 'category' : 'linear');
            base[key].push(helpers.configMerge(Chart.scaleService.getScaleDefaults(axisType), valueObj));
          });
        }
      } else if (base.hasOwnProperty(key) && typeof base[key] === "object" && base[key] !== null && typeof value === "object") {
        // If we are overwriting an object with an object, do a merge of the properties.
        base[key] = helpers.configMerge(base[key], value);

      } else {
        // can just overwrite the value in this case
        base[key] = value;
      }
    });

    return base;
  };
  helpers.getValueAtIndexOrDefault = function(value, index, defaultValue) {
    if (value === undefined || value === null) {
      return defaultValue;
    }

    if (helpers.isArray(value)) {
      return index < value.length ? value[index] : defaultValue;
    }

    return value;
  };
  helpers.getValueOrDefault = function(value, defaultValue) {
    return value === undefined ? defaultValue : value;
  };
  helpers.indexOf = Array.prototype.indexOf?
    function(array, item) { return array.indexOf(item); } :
    function(array, item) {
      for (var i = 0, ilen = array.length; i < ilen; ++i) {
        if (array[i] === item) {
          return i;
        }
      }
      return -1;
    };
  helpers.where = function(collection, filterCallback) {
    if (helpers.isArray(collection) && Array.prototype.filter) {
      return collection.filter(filterCallback);
    } else {
      var filtered = [];

      helpers.each(collection, function(item) {
        if (filterCallback(item)) {
          filtered.push(item);
        }
      });

      return filtered;
    }
  };
  helpers.findIndex = Array.prototype.findIndex?
    function(array, callback, scope) { return array.findIndex(callback, scope); } :
    function(array, callback, scope) {
      scope = scope === undefined? array : scope;
      for (var i = 0, ilen = array.length; i < ilen; ++i) {
        if (callback.call(scope, array[i], i, array)) {
          return i;
        }
      }
      return -1;
    };
  helpers.findNextWhere = function(arrayToSearch, filterCallback, startIndex) {
    // Default to start of the array
    if (startIndex === undefined || startIndex === null) {
      startIndex = -1;
    }
    for (var i = startIndex + 1; i < arrayToSearch.length; i++) {
      var currentItem = arrayToSearch[i];
      if (filterCallback(currentItem)) {
        return currentItem;
      }
    }
  };
  helpers.findPreviousWhere = function(arrayToSearch, filterCallback, startIndex) {
    // Default to end of the array
    if (startIndex === undefined || startIndex === null) {
      startIndex = arrayToSearch.length;
    }
    for (var i = startIndex - 1; i >= 0; i--) {
      var currentItem = arrayToSearch[i];
      if (filterCallback(currentItem)) {
        return currentItem;
      }
    }
  };
  helpers.inherits = function(extensions) {
    //Basic javascript inheritance based on the model created in Backbone.js
    var parent = this;
    var ChartElement = (extensions && extensions.hasOwnProperty("constructor")) ? extensions.constructor : function() {
      return parent.apply(this, arguments);
    };

    var Surrogate = function() {
      this.constructor = ChartElement;
    };
    Surrogate.prototype = parent.prototype;
    ChartElement.prototype = new Surrogate();

    ChartElement.extend = helpers.inherits;

    if (extensions) {
      helpers.extend(ChartElement.prototype, extensions);
    }

    ChartElement.__super__ = parent.prototype;

    return ChartElement;
  };
  helpers.noop = function() {};
  helpers.uid = (function() {
    var id = 0;
    return function() {
      return id++;
    };
  })();
  //-- Math methods
  helpers.isNumber = function(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
  };
  helpers.almostEquals = function(x, y, epsilon) {
    return Math.abs(x - y) < epsilon;
  };
  helpers.max = function(array) {
    return array.reduce(function(max, value) {
      if (!isNaN(value)) {
        return Math.max(max, value);
      } else {
        return max;
      }
    }, Number.NEGATIVE_INFINITY);
  };
  helpers.min = function(array) {
    return array.reduce(function(min, value) {
      if (!isNaN(value)) {
        return Math.min(min, value);
      } else {
        return min;
      }
    }, Number.POSITIVE_INFINITY);
  };
  helpers.sign = Math.sign?
    function(x) { return Math.sign(x); } :
    function(x) {
      x = +x; // convert to a number
      if (x === 0 || isNaN(x)) {
        return x;
      }
      return x > 0 ? 1 : -1;
    };
  helpers.log10 = Math.log10?
    function(x) { return Math.log10(x); } :
    function(x) {
      return Math.log(x) / Math.LN10;
    };
  helpers.toRadians = function(degrees) {
    return degrees * (Math.PI / 180);
  };
  helpers.toDegrees = function(radians) {
    return radians * (180 / Math.PI);
  };
  // Gets the angle from vertical upright to the point about a centre.
  helpers.getAngleFromPoint = function(centrePoint, anglePoint) {
    var distanceFromXCenter = anglePoint.x - centrePoint.x,
      distanceFromYCenter = anglePoint.y - centrePoint.y,
      radialDistanceFromCenter = Math.sqrt(distanceFromXCenter * distanceFromXCenter + distanceFromYCenter * distanceFromYCenter);

    var angle = Math.atan2(distanceFromYCenter, distanceFromXCenter);

    if (angle < (-0.5 * Math.PI)) {
      angle += 2.0 * Math.PI; // make sure the returned angle is in the range of (-PI/2, 3PI/2]
    }

    return {
      angle: angle,
      distance: radialDistanceFromCenter
    };
  };
  helpers.aliasPixel = function(pixelWidth) {
    return (pixelWidth % 2 === 0) ? 0 : 0.5;
  };
  helpers.splineCurve = function(firstPoint, middlePoint, afterPoint, t) {
    //Props to Rob Spencer at scaled innovation for his post on splining between points
    //http://scaledinnovation.com/analytics/splines/aboutSplines.html

    // This function must also respect "skipped" points

    var previous = firstPoint.skip ? middlePoint : firstPoint,
      current = middlePoint,
      next = afterPoint.skip ? middlePoint : afterPoint;

    var d01 = Math.sqrt(Math.pow(current.x - previous.x, 2) + Math.pow(current.y - previous.y, 2));
    var d12 = Math.sqrt(Math.pow(next.x - current.x, 2) + Math.pow(next.y - current.y, 2));

    var s01 = d01 / (d01 + d12);
    var s12 = d12 / (d01 + d12);

    // If all points are the same, s01 & s02 will be inf
    s01 = isNaN(s01) ? 0 : s01;
    s12 = isNaN(s12) ? 0 : s12;

    var fa = t * s01; // scaling factor for triangle Ta
    var fb = t * s12;

    return {
      previous: {
        x: current.x - fa * (next.x - previous.x),
        y: current.y - fa * (next.y - previous.y)
      },
      next: {
        x: current.x + fb * (next.x - previous.x),
        y: current.y + fb * (next.y - previous.y)
      }
    };
  };
  helpers.nextItem = function(collection, index, loop) {
    if (loop) {
      return index >= collection.length - 1 ? collection[0] : collection[index + 1];
    }

    return index >= collection.length - 1 ? collection[collection.length - 1] : collection[index + 1];
  };
  helpers.previousItem = function(collection, index, loop) {
    if (loop) {
      return index <= 0 ? collection[collection.length - 1] : collection[index - 1];
    }
    return index <= 0 ? collection[0] : collection[index - 1];
  };
  // Implementation of the nice number algorithm used in determining where axis labels will go
  helpers.niceNum = function(range, round) {
    var exponent = Math.floor(helpers.log10(range));
    var fraction = range / Math.pow(10, exponent);
    var niceFraction;

    if (round) {
      if (fraction < 1.5) {
        niceFraction = 1;
      } else if (fraction < 3) {
        niceFraction = 2;
      } else if (fraction < 7) {
        niceFraction = 5;
      } else {
        niceFraction = 10;
      }
    } else {
      if (fraction <= 1.0) {
        niceFraction = 1;
      } else if (fraction <= 2) {
        niceFraction = 2;
      } else if (fraction <= 5) {
        niceFraction = 5;
      } else {
        niceFraction = 10;
      }
    }

    return niceFraction * Math.pow(10, exponent);
  };
  //Easing functions adapted from Robert Penner's easing equations
  //http://www.robertpenner.com/easing/
  var easingEffects = helpers.easingEffects = {
    linear: function(t) {
      return t;
    },
    easeInQuad: function(t) {
      return t * t;
    },
    easeOutQuad: function(t) {
      return -1 * t * (t - 2);
    },
    easeInOutQuad: function(t) {
      if ((t /= 1 / 2) < 1) {
        return 1 / 2 * t * t;
      }
      return -1 / 2 * ((--t) * (t - 2) - 1);
    },
    easeInCubic: function(t) {
      return t * t * t;
    },
    easeOutCubic: function(t) {
      return 1 * ((t = t / 1 - 1) * t * t + 1);
    },
    easeInOutCubic: function(t) {
      if ((t /= 1 / 2) < 1) {
        return 1 / 2 * t * t * t;
      }
      return 1 / 2 * ((t -= 2) * t * t + 2);
    },
    easeInQuart: function(t) {
      return t * t * t * t;
    },
    easeOutQuart: function(t) {
      return -1 * ((t = t / 1 - 1) * t * t * t - 1);
    },
    easeInOutQuart: function(t) {
      if ((t /= 1 / 2) < 1) {
        return 1 / 2 * t * t * t * t;
      }
      return -1 / 2 * ((t -= 2) * t * t * t - 2);
    },
    easeInQuint: function(t) {
      return 1 * (t /= 1) * t * t * t * t;
    },
    easeOutQuint: function(t) {
      return 1 * ((t = t / 1 - 1) * t * t * t * t + 1);
    },
    easeInOutQuint: function(t) {
      if ((t /= 1 / 2) < 1) {
        return 1 / 2 * t * t * t * t * t;
      }
      return 1 / 2 * ((t -= 2) * t * t * t * t + 2);
    },
    easeInSine: function(t) {
      return -1 * Math.cos(t / 1 * (Math.PI / 2)) + 1;
    },
    easeOutSine: function(t) {
      return 1 * Math.sin(t / 1 * (Math.PI / 2));
    },
    easeInOutSine: function(t) {
      return -1 / 2 * (Math.cos(Math.PI * t / 1) - 1);
    },
    easeInExpo: function(t) {
      return (t === 0) ? 1 : 1 * Math.pow(2, 10 * (t / 1 - 1));
    },
    easeOutExpo: function(t) {
      return (t === 1) ? 1 : 1 * (-Math.pow(2, -10 * t / 1) + 1);
    },
    easeInOutExpo: function(t) {
      if (t === 0) {
        return 0;
      }
      if (t === 1) {
        return 1;
      }
      if ((t /= 1 / 2) < 1) {
        return 1 / 2 * Math.pow(2, 10 * (t - 1));
      }
      return 1 / 2 * (-Math.pow(2, -10 * --t) + 2);
    },
    easeInCirc: function(t) {
      if (t >= 1) {
        return t;
      }
      return -1 * (Math.sqrt(1 - (t /= 1) * t) - 1);
    },
    easeOutCirc: function(t) {
      return 1 * Math.sqrt(1 - (t = t / 1 - 1) * t);
    },
    easeInOutCirc: function(t) {
      if ((t /= 1 / 2) < 1) {
        return -1 / 2 * (Math.sqrt(1 - t * t) - 1);
      }
      return 1 / 2 * (Math.sqrt(1 - (t -= 2) * t) + 1);
    },
    easeInElastic: function(t) {
      var s = 1.70158;
      var p = 0;
      var a = 1;
      if (t === 0) {
        return 0;
      }
      if ((t /= 1) === 1) {
        return 1;
      }
      if (!p) {
        p = 1 * 0.3;
      }
      if (a < Math.abs(1)) {
        a = 1;
        s = p / 4;
      } else {
        s = p / (2 * Math.PI) * Math.asin(1 / a);
      }
      return -(a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * 1 - s) * (2 * Math.PI) / p));
    },
    easeOutElastic: function(t) {
      var s = 1.70158;
      var p = 0;
      var a = 1;
      if (t === 0) {
        return 0;
      }
      if ((t /= 1) === 1) {
        return 1;
      }
      if (!p) {
        p = 1 * 0.3;
      }
      if (a < Math.abs(1)) {
        a = 1;
        s = p / 4;
      } else {
        s = p / (2 * Math.PI) * Math.asin(1 / a);
      }
      return a * Math.pow(2, -10 * t) * Math.sin((t * 1 - s) * (2 * Math.PI) / p) + 1;
    },
    easeInOutElastic: function(t) {
      var s = 1.70158;
      var p = 0;
      var a = 1;
      if (t === 0) {
        return 0;
      }
      if ((t /= 1 / 2) === 2) {
        return 1;
      }
      if (!p) {
        p = 1 * (0.3 * 1.5);
      }
      if (a < Math.abs(1)) {
        a = 1;
        s = p / 4;
      } else {
        s = p / (2 * Math.PI) * Math.asin(1 / a);
      }
      if (t < 1) {
        return -0.5 * (a * Math.pow(2, 10 * (t -= 1)) * Math.sin((t * 1 - s) * (2 * Math.PI) / p));
      }
      return a * Math.pow(2, -10 * (t -= 1)) * Math.sin((t * 1 - s) * (2 * Math.PI) / p) * 0.5 + 1;
    },
    easeInBack: function(t) {
      var s = 1.70158;
      return 1 * (t /= 1) * t * ((s + 1) * t - s);
    },
    easeOutBack: function(t) {
      var s = 1.70158;
      return 1 * ((t = t / 1 - 1) * t * ((s + 1) * t + s) + 1);
    },
    easeInOutBack: function(t) {
      var s = 1.70158;
      if ((t /= 1 / 2) < 1) {
        return 1 / 2 * (t * t * (((s *= (1.525)) + 1) * t - s));
      }
      return 1 / 2 * ((t -= 2) * t * (((s *= (1.525)) + 1) * t + s) + 2);
    },
    easeInBounce: function(t) {
      return 1 - easingEffects.easeOutBounce(1 - t);
    },
    easeOutBounce: function(t) {
      if ((t /= 1) < (1 / 2.75)) {
        return 1 * (7.5625 * t * t);
      } else if (t < (2 / 2.75)) {
        return 1 * (7.5625 * (t -= (1.5 / 2.75)) * t + 0.75);
      } else if (t < (2.5 / 2.75)) {
        return 1 * (7.5625 * (t -= (2.25 / 2.75)) * t + 0.9375);
      } else {
        return 1 * (7.5625 * (t -= (2.625 / 2.75)) * t + 0.984375);
      }
    },
    easeInOutBounce: function(t) {
      if (t < 1 / 2) {
        return easingEffects.easeInBounce(t * 2) * 0.5;
      }
      return easingEffects.easeOutBounce(t * 2 - 1) * 0.5 + 1 * 0.5;
    }
  };
  //Request animation polyfill - http://www.paulirish.com/2011/requestanimationframe-for-smart-animating/
  helpers.requestAnimFrame = (function() {
    return window.requestAnimationFrame ||
      window.webkitRequestAnimationFrame ||
      window.mozRequestAnimationFrame ||
      window.oRequestAnimationFrame ||
      window.msRequestAnimationFrame ||
      function(callback) {
        return window.setTimeout(callback, 1000 / 60);
      };
  })();
  helpers.cancelAnimFrame = (function() {
    return window.cancelAnimationFrame ||
      window.webkitCancelAnimationFrame ||
      window.mozCancelAnimationFrame ||
      window.oCancelAnimationFrame ||
      window.msCancelAnimationFrame ||
      function(callback) {
        return window.clearTimeout(callback, 1000 / 60);
      };
  })();
  //-- DOM methods
  helpers.getRelativePosition = function(evt, chart) {
    var mouseX, mouseY;
    var e = evt.originalEvent || evt,
      canvas = evt.currentTarget || evt.srcElement,
      boundingRect = canvas.getBoundingClientRect();

    var touches = e.touches;
    if (touches && touches.length > 0) {
      mouseX = touches[0].clientX;
      mouseY = touches[0].clientY;

    } else {
      mouseX = e.clientX;
      mouseY = e.clientY;
    }

    // Scale mouse coordinates into canvas coordinates
    // by following the pattern laid out by 'jerryj' in the comments of
    // http://www.html5canvastutorials.com/advanced/html5-canvas-mouse-coordinates/
    var paddingLeft = parseFloat(helpers.getStyle(canvas, 'padding-left'));
    var paddingTop = parseFloat(helpers.getStyle(canvas, 'padding-top'));
    var paddingRight = parseFloat(helpers.getStyle(canvas, 'padding-right'));
    var paddingBottom = parseFloat(helpers.getStyle(canvas, 'padding-bottom'));
    var width = boundingRect.right - boundingRect.left - paddingLeft - paddingRight;
    var height = boundingRect.bottom - boundingRect.top - paddingTop - paddingBottom;

    // We divide by the current device pixel ratio, because the canvas is scaled up by that amount in each direction. However
    // the backend model is in unscaled coordinates. Since we are going to deal with our model coordinates, we go back here
    mouseX = Math.round((mouseX - boundingRect.left - paddingLeft) / (width) * canvas.width / chart.currentDevicePixelRatio);
    mouseY = Math.round((mouseY - boundingRect.top - paddingTop) / (height) * canvas.height / chart.currentDevicePixelRatio);

    return {
      x: mouseX,
      y: mouseY
    };

  };
  helpers.addEvent = function(node, eventType, method) {
    if (node.addEventListener) {
      node.addEventListener(eventType, method);
    } else if (node.attachEvent) {
      node.attachEvent("on" + eventType, method);
    } else {
      node["on" + eventType] = method;
    }
  };
  helpers.removeEvent = function(node, eventType, handler) {
    if (node.removeEventListener) {
      node.removeEventListener(eventType, handler, false);
    } else if (node.detachEvent) {
      node.detachEvent("on" + eventType, handler);
    } else {
      node["on" + eventType] = helpers.noop;
    }
  };
  helpers.bindEvents = function(chartInstance, arrayOfEvents, handler) {
    // Create the events object if it's not already present
    var events = chartInstance.events = chartInstance.events || {};

    helpers.each(arrayOfEvents, function(eventName) {
      events[eventName] = function() {
        handler.apply(chartInstance, arguments);
      };
      helpers.addEvent(chartInstance.chart.canvas, eventName, events[eventName]);
    });
  };
  helpers.unbindEvents = function(chartInstance, arrayOfEvents) {
    var canvas = chartInstance.chart.canvas;
    helpers.each(arrayOfEvents, function(handler, eventName) {
      helpers.removeEvent(canvas, eventName, handler);
    });
  };

  // Private helper function to convert max-width/max-height values that may be percentages into a number
  function parseMaxStyle(styleValue, node, parentProperty) {
    var valueInPixels;
    if (typeof(styleValue) === 'string') {
      valueInPixels = parseInt(styleValue, 10);

      if (styleValue.indexOf('%') != -1) {
        // percentage * size in dimension
        valueInPixels = valueInPixels / 100 * node.parentNode[parentProperty];
      }
    } else {
      valueInPixels = styleValue;
    }

    return valueInPixels;
  }

  /**
   * Returns if the given value contains an effective constraint.
   * @private
   */
  function isConstrainedValue(value) {
    return value !== undefined &&  value !== null && value !== 'none';
  }

  // Private helper to get a constraint dimension
  // @param domNode : the node to check the constraint on
  // @param maxStyle : the style that defines the maximum for the direction we are using (maxWidth / maxHeight)
  // @param percentageProperty : property of parent to use when calculating width as a percentage
  // @see http://www.nathanaeljones.com/blog/2013/reading-max-width-cross-browser
  function getConstraintDimension(domNode, maxStyle, percentageProperty) {
    var view = document.defaultView;
    var parentNode = domNode.parentNode;
    var constrainedNode = view.getComputedStyle(domNode)[maxStyle];
    var constrainedContainer = view.getComputedStyle(parentNode)[maxStyle];
    var hasCNode = isConstrainedValue(constrainedNode);
    var hasCContainer = isConstrainedValue(constrainedContainer);
    var infinity = Number.POSITIVE_INFINITY;

    if (hasCNode || hasCContainer) {
      return Math.min(
        hasCNode? parseMaxStyle(constrainedNode, domNode, percentageProperty) : infinity,
        hasCContainer? parseMaxStyle(constrainedContainer, parentNode, percentageProperty) : infinity);
    }

    return 'none';
  }
  // returns Number or undefined if no constraint
  helpers.getConstraintWidth = function(domNode) {
    return getConstraintDimension(domNode, 'max-width', 'clientWidth');
  };
  // returns Number or undefined if no constraint
  helpers.getConstraintHeight = function(domNode) {
    return getConstraintDimension(domNode, 'max-height', 'clientHeight');
  };
  helpers.getMaximumWidth = function(domNode) {
    var container = domNode.parentNode;
    var padding = parseInt(helpers.getStyle(container, 'padding-left')) + parseInt(helpers.getStyle(container, 'padding-right'));
    var w = container.clientWidth - padding;
    var cw = helpers.getConstraintWidth(domNode);
    return isNaN(cw)? w : Math.min(w, cw);
  };
  helpers.getMaximumHeight = function(domNode) {
    var container = domNode.parentNode;
    var padding = parseInt(helpers.getStyle(container, 'padding-top')) + parseInt(helpers.getStyle(container, 'padding-bottom'));
    var h = container.clientHeight - padding;
    var ch = helpers.getConstraintHeight(domNode);
    return isNaN(ch)? h : Math.min(h, ch);
  };
  helpers.getStyle = function(el, property) {
    return el.currentStyle ?
      el.currentStyle[property] :
      document.defaultView.getComputedStyle(el, null).getPropertyValue(property);
  };
  helpers.retinaScale = function(chart) {
    var ctx = chart.ctx;
    var canvas = chart.canvas;
    var width = canvas.width;
    var height = canvas.height;
    var pixelRatio = chart.currentDevicePixelRatio = window.devicePixelRatio || 1;

    if (pixelRatio !== 1) {
      canvas.height = height * pixelRatio;
      canvas.width = width * pixelRatio;
      ctx.scale(pixelRatio, pixelRatio);

      // Store the device pixel ratio so that we can go backwards in `destroy`.
      // The devicePixelRatio changes with zoom, so there are no guarantees that it is the same
      // when destroy is called
      chart.originalDevicePixelRatio = chart.originalDevicePixelRatio || pixelRatio;
    }

    canvas.style.width = width + 'px';
    canvas.style.height = height + 'px';
  };
  //-- Canvas methods
  helpers.clear = function(chart) {
    chart.ctx.clearRect(0, 0, chart.width, chart.height);
  };
  helpers.fontString = function(pixelSize, fontStyle, fontFamily) {
    return fontStyle + " " + pixelSize + "px " + fontFamily;
  };
  helpers.longestText = function(ctx, font, arrayOfThings, cache) {
    cache = cache || {};
    var data = cache.data = cache.data || {};
    var gc = cache.garbageCollect = cache.garbageCollect || [];

    if (cache.font !== font) {
      data = cache.data = {};
      gc = cache.garbageCollect = [];
      cache.font = font;
    }

    ctx.font = font;
    var longest = 0;
    helpers.each(arrayOfThings, function(thing) {
      // Undefined strings and arrays should not be measured
      if (thing !== undefined && thing !== null && helpers.isArray(thing) !== true) {
        longest = helpers.measureText(ctx, data, gc, longest, thing);
      } else if (helpers.isArray(thing)) {
        // if it is an array lets measure each element
        // to do maybe simplify this function a bit so we can do this more recursively?
        helpers.each(thing, function(nestedThing) {
          // Undefined strings and arrays should not be measured
          if (nestedThing !== undefined && nestedThing !== null && !helpers.isArray(nestedThing)) {
            longest = helpers.measureText(ctx, data, gc, longest, nestedThing);
          }
        });
      }
    });

    var gcLen = gc.length / 2;
    if (gcLen > arrayOfThings.length) {
      for (var i = 0; i < gcLen; i++) {
        delete data[gc[i]];
      }
      gc.splice(0, gcLen);
    }
    return longest;
  };
  helpers.measureText = function (ctx, data, gc, longest, string) {
    var textWidth = data[string];
    if (!textWidth) {
      textWidth = data[string] = ctx.measureText(string).width;
      gc.push(string);
    }
    if (textWidth > longest) {
      longest = textWidth;
    }
    return longest;
  };
  helpers.numberOfLabelLines = function(arrayOfThings) {
    var numberOfLines = 1;
    helpers.each(arrayOfThings, function(thing) {
      if (helpers.isArray(thing)) {
        if (thing.length > numberOfLines) {
          numberOfLines = thing.length;
        }
      }
    });
    return numberOfLines;
  };
  helpers.drawRoundedRectangle = function(ctx, x, y, width, height, radius) {
    ctx.beginPath();
    ctx.moveTo(x + radius, y);
    ctx.lineTo(x + width - radius, y);
    ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
    ctx.lineTo(x + width, y + height - radius);
    ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height);
    ctx.lineTo(x + radius, y + height);
    ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
    ctx.lineTo(x, y + radius);
    ctx.quadraticCurveTo(x, y, x + radius, y);
    ctx.closePath();
  };
  helpers.color = function(c) {
    if (!color) {
      console.log('Color.js not found!');
      return c;
    }

    /* global CanvasGradient */
    if (c instanceof CanvasGradient) {
      return color(Chart.defaults.global.defaultColor);
    }

    return color(c);
  };
  helpers.addResizeListener = function(node, callback) {
    // Hide an iframe before the node
    var hiddenIframe = document.createElement('iframe');
    var hiddenIframeClass = 'chartjs-hidden-iframe';

    if (hiddenIframe.classlist) {
      // can use classlist
      hiddenIframe.classlist.add(hiddenIframeClass);
    } else {
      hiddenIframe.setAttribute('class', hiddenIframeClass);
    }

    // Set the style
    var style = hiddenIframe.style;
    style.width = '100%';
    style.display = 'block';
    style.border = 0;
    style.height = 0;
    style.margin = 0;
    style.position = 'absolute';
    style.left = 0;
    style.right = 0;
    style.top = 0;
    style.bottom = 0;

    // Insert the iframe so that contentWindow is available
    node.insertBefore(hiddenIframe, node.firstChild);

    (hiddenIframe.contentWindow || hiddenIframe).onresize = function() {
      if (callback) {
        callback();
      }
    };
  };
  helpers.removeResizeListener = function(node) {
    var hiddenIframe = node.querySelector('.chartjs-hidden-iframe');

    // Remove the resize detect iframe
    if (hiddenIframe) {
      hiddenIframe.parentNode.removeChild(hiddenIframe);
    }
  };
  helpers.isArray = Array.isArray?
    function(obj) { return Array.isArray(obj); } :
    function(obj) {
      return Object.prototype.toString.call(obj) === '[object Array]';
    };
  //! @see http://stackoverflow.com/a/14853974
  helpers.arrayEquals = function(a0, a1) {
    var i, ilen, v0, v1;

    if (!a0 || !a1 || a0.length != a1.length) {
      return false;
    }

    for (i = 0, ilen=a0.length; i < ilen; ++i) {
      v0 = a0[i];
      v1 = a1[i];

      if (v0 instanceof Array && v1 instanceof Array) {
        if (!helpers.arrayEquals(v0, v1)) {
          return false;
        }
      } else if (v0 != v1) {
        // NOTE: two different object instances will never be equal: {x:20} != {x:20}
        return false;
      }
    }

    return true;
  };
  helpers.callCallback = function(fn, args, _tArg) {
    if (fn && typeof fn.call === 'function') {
      fn.apply(_tArg, args);
    }
  };
  helpers.getHoverColor = function(color) {
    /* global CanvasPattern */
    return (color instanceof CanvasPattern) ?
      color :
      helpers.color(color).saturate(0.5).darken(0.1).rgbString();
  };
};

},{"3":3}],27:[function(require,module,exports){
"use strict";

module.exports = function() {

  //Occupy the global variable of Chart, and create a simple base class
  var Chart = function(context, config) {
    var me = this;
    var helpers = Chart.helpers;
    me.config = config || {
      data: {
        datasets: []
      }
    };

    // Support a jQuery'd canvas element
    if (context.length && context[0].getContext) {
      context = context[0];
    }

    // Support a canvas domnode
    if (context.getContext) {
      context = context.getContext("2d");
    }

    me.ctx = context;
    me.canvas = context.canvas;

    context.canvas.style.display = context.canvas.style.display || 'block';

    // Figure out what the size of the chart will be.
    // If the canvas has a specified width and height, we use those else
    // we look to see if the canvas node has a CSS width and height.
    // If there is still no height, fill the parent container
    me.width = context.canvas.width || parseInt(helpers.getStyle(context.canvas, 'width'), 10) || helpers.getMaximumWidth(context.canvas);
    me.height = context.canvas.height || parseInt(helpers.getStyle(context.canvas, 'height'), 10) || helpers.getMaximumHeight(context.canvas);

    me.aspectRatio = me.width / me.height;

    if (isNaN(me.aspectRatio) || isFinite(me.aspectRatio) === false) {
      // If the canvas has no size, try and figure out what the aspect ratio will be.
      // Some charts prefer square canvases (pie, radar, etc). If that is specified, use that
      // else use the canvas default ratio of 2
      me.aspectRatio = config.aspectRatio !== undefined ? config.aspectRatio : 2;
    }

    // Store the original style of the element so we can set it back
    me.originalCanvasStyleWidth = context.canvas.style.width;
    me.originalCanvasStyleHeight = context.canvas.style.height;

    // High pixel density displays - multiply the size of the canvas height/width by the device pixel ratio, then scale.
    helpers.retinaScale(me);
    me.controller = new Chart.Controller(me);

    // Always bind this so that if the responsive state changes we still work
    helpers.addResizeListener(context.canvas.parentNode, function() {
      if (me.controller && me.controller.config.options.responsive) {
        me.controller.resize();
      }
    });

    return me.controller ? me.controller : me;

  };

  //Globally expose the defaults to allow for user updating/changing
  Chart.defaults = {
    global: {
      responsive: true,
      responsiveAnimationDuration: 0,
      maintainAspectRatio: true,
      events: ["mousemove", "mouseout", "click", "touchstart", "touchmove"],
      hover: {
        onHover: null,
        mode: 'single',
        animationDuration: 400
      },
      onClick: null,
      defaultColor: 'rgba(0,0,0,0.1)',
      defaultFontColor: '#666',
      defaultFontFamily: "'Helvetica Neue', 'Helvetica', 'Arial', sans-serif",
      defaultFontSize: 12,
      defaultFontStyle: 'normal',
      showLines: true,

      // Element defaults defined in element extensions
      elements: {},

      // Legend callback string
      legendCallback: function(chart) {
        var text = [];
        text.push('<ul class="' + chart.id + '-legend">');
        for (var i = 0; i < chart.data.datasets.length; i++) {
          text.push('<li><span style="background-color:' + chart.data.datasets[i].backgroundColor + '"></span>');
          if (chart.data.datasets[i].label) {
            text.push(chart.data.datasets[i].label);
          }
          text.push('</li>');
        }
        text.push('</ul>');

        return text.join("");
      }
    }
  };

  Chart.Chart = Chart;

  return Chart;

};

},{}],28:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  // The layout service is very self explanatory.  It's responsible for the layout within a chart.
  // Scales, Legends and Plugins all rely on the layout service and can easily register to be placed anywhere they need
  // It is this service's responsibility of carrying out that layout.
  Chart.layoutService = {
    defaults: {},

    // Register a box to a chartInstance. A box is simply a reference to an object that requires layout. eg. Scales, Legend, Plugins.
    addBox: function(chartInstance, box) {
      if (!chartInstance.boxes) {
        chartInstance.boxes = [];
      }
      chartInstance.boxes.push(box);
    },

    removeBox: function(chartInstance, box) {
      if (!chartInstance.boxes) {
        return;
      }
      chartInstance.boxes.splice(chartInstance.boxes.indexOf(box), 1);
    },

    // The most important function
    update: function(chartInstance, width, height) {

      if (!chartInstance) {
        return;
      }

      var xPadding = 0;
      var yPadding = 0;

      var leftBoxes = helpers.where(chartInstance.boxes, function(box) {
        return box.options.position === "left";
      });
      var rightBoxes = helpers.where(chartInstance.boxes, function(box) {
        return box.options.position === "right";
      });
      var topBoxes = helpers.where(chartInstance.boxes, function(box) {
        return box.options.position === "top";
      });
      var bottomBoxes = helpers.where(chartInstance.boxes, function(box) {
        return box.options.position === "bottom";
      });

      // Boxes that overlay the chartarea such as the radialLinear scale
      var chartAreaBoxes = helpers.where(chartInstance.boxes, function(box) {
        return box.options.position === "chartArea";
      });

      // Ensure that full width boxes are at the very top / bottom
      topBoxes.sort(function(a, b) {
        return (b.options.fullWidth ? 1 : 0) - (a.options.fullWidth ? 1 : 0);
      });
      bottomBoxes.sort(function(a, b) {
        return (a.options.fullWidth ? 1 : 0) - (b.options.fullWidth ? 1 : 0);
      });

      // Essentially we now have any number of boxes on each of the 4 sides.
      // Our canvas looks like the following.
      // The areas L1 and L2 are the left axes. R1 is the right axis, T1 is the top axis and
      // B1 is the bottom axis
      // There are also 4 quadrant-like locations (left to right instead of clockwise) reserved for chart overlays
      // These locations are single-box locations only, when trying to register a chartArea location that is already taken,
      // an error will be thrown.
      //
      // |----------------------------------------------------|
      // |                  T1 (Full Width)                   |
      // |----------------------------------------------------|
      // |    |    |                 T2                  |    |
      // |    |----|-------------------------------------|----|
      // |    |    | C1 |                           | C2 |    |
      // |    |    |----|                           |----|    |
      // |    |    |                                     |    |
      // | L1 | L2 |           ChartArea (C0)            | R1 |
      // |    |    |                                     |    |
      // |    |    |----|                           |----|    |
      // |    |    | C3 |                           | C4 |    |
      // |    |----|-------------------------------------|----|
      // |    |    |                 B1                  |    |
      // |----------------------------------------------------|
      // |                  B2 (Full Width)                   |
      // |----------------------------------------------------|
      //
      // What we do to find the best sizing, we do the following
      // 1. Determine the minimum size of the chart area.
      // 2. Split the remaining width equally between each vertical axis
      // 3. Split the remaining height equally between each horizontal axis
      // 4. Give each layout the maximum size it can be. The layout will return it's minimum size
      // 5. Adjust the sizes of each axis based on it's minimum reported size.
      // 6. Refit each axis
      // 7. Position each axis in the final location
      // 8. Tell the chart the final location of the chart area
      // 9. Tell any axes that overlay the chart area the positions of the chart area

      // Step 1
      var chartWidth = width - (2 * xPadding);
      var chartHeight = height - (2 * yPadding);
      var chartAreaWidth = chartWidth / 2; // min 50%
      var chartAreaHeight = chartHeight / 2; // min 50%

      // Step 2
      var verticalBoxWidth = (width - chartAreaWidth) / (leftBoxes.length + rightBoxes.length);

      // Step 3
      var horizontalBoxHeight = (height - chartAreaHeight) / (topBoxes.length + bottomBoxes.length);

      // Step 4
      var maxChartAreaWidth = chartWidth;
      var maxChartAreaHeight = chartHeight;
      var minBoxSizes = [];

      helpers.each(leftBoxes.concat(rightBoxes, topBoxes, bottomBoxes), getMinimumBoxSize);

      function getMinimumBoxSize(box) {
        var minSize;
        var isHorizontal = box.isHorizontal();

        if (isHorizontal) {
          minSize = box.update(box.options.fullWidth ? chartWidth : maxChartAreaWidth, horizontalBoxHeight);
          maxChartAreaHeight -= minSize.height;
        } else {
          minSize = box.update(verticalBoxWidth, chartAreaHeight);
          maxChartAreaWidth -= minSize.width;
        }

        minBoxSizes.push({
          horizontal: isHorizontal,
          minSize: minSize,
          box: box
        });
      }

      // At this point, maxChartAreaHeight and maxChartAreaWidth are the size the chart area could
      // be if the axes are drawn at their minimum sizes.

      // Steps 5 & 6
      var totalLeftBoxesWidth = xPadding;
      var totalRightBoxesWidth = xPadding;
      var totalTopBoxesHeight = yPadding;
      var totalBottomBoxesHeight = yPadding;

      // Update, and calculate the left and right margins for the horizontal boxes
      helpers.each(leftBoxes.concat(rightBoxes), fitBox);

      helpers.each(leftBoxes, function(box) {
        totalLeftBoxesWidth += box.width;
      });

      helpers.each(rightBoxes, function(box) {
        totalRightBoxesWidth += box.width;
      });

      // Set the Left and Right margins for the horizontal boxes
      helpers.each(topBoxes.concat(bottomBoxes), fitBox);

      // Function to fit a box
      function fitBox(box) {
        var minBoxSize = helpers.findNextWhere(minBoxSizes, function(minBoxSize) {
          return minBoxSize.box === box;
        });

        if (minBoxSize) {
          if (box.isHorizontal()) {
            var scaleMargin = {
              left: totalLeftBoxesWidth,
              right: totalRightBoxesWidth,
              top: 0,
              bottom: 0
            };

            // Don't use min size here because of label rotation. When the labels are rotated, their rotation highly depends
            // on the margin. Sometimes they need to increase in size slightly
            box.update(box.options.fullWidth ? chartWidth : maxChartAreaWidth, chartHeight / 2, scaleMargin);
          } else {
            box.update(minBoxSize.minSize.width, maxChartAreaHeight);
          }
        }
      }

      // Figure out how much margin is on the top and bottom of the vertical boxes
      helpers.each(topBoxes, function(box) {
        totalTopBoxesHeight += box.height;
      });

      helpers.each(bottomBoxes, function(box) {
        totalBottomBoxesHeight += box.height;
      });

      // Let the left layout know the final margin
      helpers.each(leftBoxes.concat(rightBoxes), finalFitVerticalBox);

      function finalFitVerticalBox(box) {
        var minBoxSize = helpers.findNextWhere(minBoxSizes, function(minBoxSize) {
          return minBoxSize.box === box;
        });

        var scaleMargin = {
          left: 0,
          right: 0,
          top: totalTopBoxesHeight,
          bottom: totalBottomBoxesHeight
        };

        if (minBoxSize) {
          box.update(minBoxSize.minSize.width, maxChartAreaHeight, scaleMargin);
        }
      }

      // Recalculate because the size of each layout might have changed slightly due to the margins (label rotation for instance)
      totalLeftBoxesWidth = xPadding;
      totalRightBoxesWidth = xPadding;
      totalTopBoxesHeight = yPadding;
      totalBottomBoxesHeight = yPadding;

      helpers.each(leftBoxes, function(box) {
        totalLeftBoxesWidth += box.width;
      });

      helpers.each(rightBoxes, function(box) {
        totalRightBoxesWidth += box.width;
      });

      helpers.each(topBoxes, function(box) {
        totalTopBoxesHeight += box.height;
      });
      helpers.each(bottomBoxes, function(box) {
        totalBottomBoxesHeight += box.height;
      });

      // Figure out if our chart area changed. This would occur if the dataset layout label rotation
      // changed due to the application of the margins in step 6. Since we can only get bigger, this is safe to do
      // without calling `fit` again
      var newMaxChartAreaHeight = height - totalTopBoxesHeight - totalBottomBoxesHeight;
      var newMaxChartAreaWidth = width - totalLeftBoxesWidth - totalRightBoxesWidth;

      if (newMaxChartAreaWidth !== maxChartAreaWidth || newMaxChartAreaHeight !== maxChartAreaHeight) {
        helpers.each(leftBoxes, function(box) {
          box.height = newMaxChartAreaHeight;
        });

        helpers.each(rightBoxes, function(box) {
          box.height = newMaxChartAreaHeight;
        });

        helpers.each(topBoxes, function(box) {
          if (!box.options.fullWidth) {
            box.width = newMaxChartAreaWidth;
          }
        });

        helpers.each(bottomBoxes, function(box) {
          if (!box.options.fullWidth) {
            box.width = newMaxChartAreaWidth;
          }
        });

        maxChartAreaHeight = newMaxChartAreaHeight;
        maxChartAreaWidth = newMaxChartAreaWidth;
      }

      // Step 7 - Position the boxes
      var left = xPadding;
      var top = yPadding;

      helpers.each(leftBoxes.concat(topBoxes), placeBox);

      // Account for chart width and height
      left += maxChartAreaWidth;
      top += maxChartAreaHeight;

      helpers.each(rightBoxes, placeBox);
      helpers.each(bottomBoxes, placeBox);

      function placeBox(box) {
        if (box.isHorizontal()) {
          box.left = box.options.fullWidth ? xPadding : totalLeftBoxesWidth;
          box.right = box.options.fullWidth ? width - xPadding : totalLeftBoxesWidth + maxChartAreaWidth;
          box.top = top;
          box.bottom = top + box.height;

          // Move to next point
          top = box.bottom;

        } else {

          box.left = left;
          box.right = left + box.width;
          box.top = totalTopBoxesHeight;
          box.bottom = totalTopBoxesHeight + maxChartAreaHeight;

          // Move to next point
          left = box.right;
        }
      }

      // Step 8
      chartInstance.chartArea = {
        left: totalLeftBoxesWidth,
        top: totalTopBoxesHeight,
        right: totalLeftBoxesWidth + maxChartAreaWidth,
        bottom: totalTopBoxesHeight + maxChartAreaHeight
      };

      // Step 9
      helpers.each(chartAreaBoxes, function(box) {
        box.left = chartInstance.chartArea.left;
        box.top = chartInstance.chartArea.top;
        box.right = chartInstance.chartArea.right;
        box.bottom = chartInstance.chartArea.bottom;

        box.update(maxChartAreaWidth, maxChartAreaHeight);
      });
    }
  };
};

},{}],29:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;
  var noop = helpers.noop;

  Chart.defaults.global.legend = {

    display: true,
    position: 'top',
    fullWidth: true, // marks that this box should take the full width of the canvas (pushing down other boxes)
    reverse: false,

    // a callback that will handle
    onClick: function(e, legendItem) {
      var index = legendItem.datasetIndex;
      var ci = this.chart;
      var meta = ci.getDatasetMeta(index);

      // See controller.isDatasetVisible comment
      meta.hidden = meta.hidden === null? !ci.data.datasets[index].hidden : null;

      // We hid a dataset ... rerender the chart
      ci.update();
    },

    labels: {
      boxWidth: 40,
      padding: 10,
      // Generates labels shown in the legend
      // Valid properties to return:
      // text : text to display
      // fillStyle : fill of coloured box
      // strokeStyle: stroke of coloured box
      // hidden : if this legend item refers to a hidden item
      // lineCap : cap style for line
      // lineDash
      // lineDashOffset :
      // lineJoin :
      // lineWidth :
      generateLabels: function(chart) {
        var data = chart.data;
        return helpers.isArray(data.datasets) ? data.datasets.map(function(dataset, i) {
          return {
            text: dataset.label,
            fillStyle: (!helpers.isArray(dataset.backgroundColor) ? dataset.backgroundColor : dataset.backgroundColor[0]),
            hidden: !chart.isDatasetVisible(i),
            lineCap: dataset.borderCapStyle,
            lineDash: dataset.borderDash,
            lineDashOffset: dataset.borderDashOffset,
            lineJoin: dataset.borderJoinStyle,
            lineWidth: dataset.borderWidth,
            strokeStyle: dataset.borderColor,
            pointStyle: dataset.pointStyle,

            // Below is extra data used for toggling the datasets
            datasetIndex: i
          };
        }, this) : [];
      }
    }
  };

  Chart.Legend = Chart.Element.extend({

    initialize: function(config) {
      helpers.extend(this, config);

      // Contains hit boxes for each dataset (in dataset order)
      this.legendHitBoxes = [];

      // Are we in doughnut mode which has a different data type
      this.doughnutMode = false;
    },

    // These methods are ordered by lifecyle. Utilities then follow.
    // Any function defined here is inherited by all legend types.
    // Any function can be extended by the legend type

    beforeUpdate: noop,
    update: function(maxWidth, maxHeight, margins) {
      var me = this;

      // Update Lifecycle - Probably don't want to ever extend or overwrite this function ;)
      me.beforeUpdate();

      // Absorb the master measurements
      me.maxWidth = maxWidth;
      me.maxHeight = maxHeight;
      me.margins = margins;

      // Dimensions
      me.beforeSetDimensions();
      me.setDimensions();
      me.afterSetDimensions();
      // Labels
      me.beforeBuildLabels();
      me.buildLabels();
      me.afterBuildLabels();

      // Fit
      me.beforeFit();
      me.fit();
      me.afterFit();
      //
      me.afterUpdate();

      return me.minSize;
    },
    afterUpdate: noop,

    //

    beforeSetDimensions: noop,
    setDimensions: function() {
      var me = this;
      // Set the unconstrained dimension before label rotation
      if (me.isHorizontal()) {
        // Reset position before calculating rotation
        me.width = me.maxWidth;
        me.left = 0;
        me.right = me.width;
      } else {
        me.height = me.maxHeight;

        // Reset position before calculating rotation
        me.top = 0;
        me.bottom = me.height;
      }

      // Reset padding
      me.paddingLeft = 0;
      me.paddingTop = 0;
      me.paddingRight = 0;
      me.paddingBottom = 0;

      // Reset minSize
      me.minSize = {
        width: 0,
        height: 0
      };
    },
    afterSetDimensions: noop,

    //

    beforeBuildLabels: noop,
    buildLabels: function() {
      var me = this;
      me.legendItems = me.options.labels.generateLabels.call(me, me.chart);
      if(me.options.reverse){
        me.legendItems.reverse();
      }
    },
    afterBuildLabels: noop,

    //

    beforeFit: noop,
    fit: function() {
      var me = this;
      var opts = me.options;
      var labelOpts = opts.labels;
      var display = opts.display;

      var ctx = me.ctx;

      var globalDefault = Chart.defaults.global,
        itemOrDefault = helpers.getValueOrDefault,
        fontSize = itemOrDefault(labelOpts.fontSize, globalDefault.defaultFontSize),
        fontStyle = itemOrDefault(labelOpts.fontStyle, globalDefault.defaultFontStyle),
        fontFamily = itemOrDefault(labelOpts.fontFamily, globalDefault.defaultFontFamily),
        labelFont = helpers.fontString(fontSize, fontStyle, fontFamily);

      // Reset hit boxes
      var hitboxes = me.legendHitBoxes = [];

      var minSize = me.minSize;
      var isHorizontal = me.isHorizontal();

      if (isHorizontal) {
        minSize.width = me.maxWidth; // fill all the width
        minSize.height = display ? 10 : 0;
      } else {
        minSize.width = display ? 10 : 0;
        minSize.height = me.maxHeight; // fill all the height
      }

      // Increase sizes here
      if (display) {
        ctx.font = labelFont;

        if (isHorizontal) {
          // Labels

          // Width of each line of legend boxes. Labels wrap onto multiple lines when there are too many to fit on one
          var lineWidths = me.lineWidths = [0];
          var totalHeight = me.legendItems.length ? fontSize + (labelOpts.padding) : 0;

          ctx.textAlign = "left";
          ctx.textBaseline = 'top';

          helpers.each(me.legendItems, function(legendItem, i) {
            var boxWidth = labelOpts.usePointStyle ?
              fontSize * Math.sqrt(2) :
              labelOpts.boxWidth;

            var width = boxWidth + (fontSize / 2) + ctx.measureText(legendItem.text).width;
            if (lineWidths[lineWidths.length - 1] + width + labelOpts.padding >= me.width) {
              totalHeight += fontSize + (labelOpts.padding);
              lineWidths[lineWidths.length] = me.left;
            }

            // Store the hitbox width and height here. Final position will be updated in `draw`
            hitboxes[i] = {
              left: 0,
              top: 0,
              width: width,
              height: fontSize
            };

            lineWidths[lineWidths.length - 1] += width + labelOpts.padding;
          });

          minSize.height += totalHeight;

        } else {
          var vPadding = labelOpts.padding;
          var columnWidths = me.columnWidths = [];
          var totalWidth = labelOpts.padding;
          var currentColWidth = 0;
          var currentColHeight = 0;
          var itemHeight = fontSize + vPadding;

          helpers.each(me.legendItems, function(legendItem, i) {
            // If usePointStyle is set, multiple boxWidth by 2 since it represents
            // the radius and not truly the width
            var boxWidth = labelOpts.usePointStyle ? 2 * labelOpts.boxWidth : labelOpts.boxWidth;

            var itemWidth = boxWidth + (fontSize / 2) + ctx.measureText(legendItem.text).width;

            // If too tall, go to new column
            if (currentColHeight + itemHeight > minSize.height) {
              totalWidth += currentColWidth + labelOpts.padding;
              columnWidths.push(currentColWidth); // previous column width

              currentColWidth = 0;
              currentColHeight = 0;
            }

            // Get max width
            currentColWidth = Math.max(currentColWidth, itemWidth);
            currentColHeight += itemHeight;

            // Store the hitbox width and height here. Final position will be updated in `draw`
            hitboxes[i] = {
              left: 0,
              top: 0,
              width: itemWidth,
              height: fontSize
            };
          });

          totalWidth += currentColWidth;
          columnWidths.push(currentColWidth);
          minSize.width += totalWidth;
        }
      }

      me.width = minSize.width;
      me.height = minSize.height;
    },
    afterFit: noop,

    // Shared Methods
    isHorizontal: function() {
      return this.options.position === "top" || this.options.position === "bottom";
    },

    // Actualy draw the legend on the canvas
    draw: function() {
      var me = this;
      var opts = me.options;
      var labelOpts = opts.labels;
      var globalDefault = Chart.defaults.global,
        lineDefault = globalDefault.elements.line,
        legendWidth = me.width,
        lineWidths = me.lineWidths;

      if (opts.display) {
        var ctx = me.ctx,
          cursor,
          itemOrDefault = helpers.getValueOrDefault,
          fontColor = itemOrDefault(labelOpts.fontColor, globalDefault.defaultFontColor),
          fontSize = itemOrDefault(labelOpts.fontSize, globalDefault.defaultFontSize),
          fontStyle = itemOrDefault(labelOpts.fontStyle, globalDefault.defaultFontStyle),
          fontFamily = itemOrDefault(labelOpts.fontFamily, globalDefault.defaultFontFamily),
          labelFont = helpers.fontString(fontSize, fontStyle, fontFamily);

        // Canvas setup
        ctx.textAlign = "left";
        ctx.textBaseline = 'top';
        ctx.lineWidth = 0.5;
        ctx.strokeStyle = fontColor; // for strikethrough effect
        ctx.fillStyle = fontColor; // render in correct colour
        ctx.font = labelFont;

        var boxWidth = labelOpts.boxWidth,
          hitboxes = me.legendHitBoxes;

        // current position
        var drawLegendBox = function(x, y, legendItem) {
          if (isNaN(boxWidth) || boxWidth <= 0) {
            return;
          }

          // Set the ctx for the box
          ctx.save();

          ctx.fillStyle = itemOrDefault(legendItem.fillStyle, globalDefault.defaultColor);
          ctx.lineCap = itemOrDefault(legendItem.lineCap, lineDefault.borderCapStyle);
          ctx.lineDashOffset = itemOrDefault(legendItem.lineDashOffset, lineDefault.borderDashOffset);
          ctx.lineJoin = itemOrDefault(legendItem.lineJoin, lineDefault.borderJoinStyle);
          ctx.lineWidth = itemOrDefault(legendItem.lineWidth, lineDefault.borderWidth);
          ctx.strokeStyle = itemOrDefault(legendItem.strokeStyle, globalDefault.defaultColor);

          if (ctx.setLineDash) {
            // IE 9 and 10 do not support line dash
            ctx.setLineDash(itemOrDefault(legendItem.lineDash, lineDefault.borderDash));
          }

          if (opts.labels && opts.labels.usePointStyle) {
            // Recalulate x and y for drawPoint() because its expecting
            // x and y to be center of figure (instead of top left)
            var radius = fontSize * Math.SQRT2 / 2;
            var offSet = radius / Math.SQRT2;
            var centerX = x + offSet;
            var centerY = y + offSet;

            // Draw pointStyle as legend symbol
            Chart.canvasHelpers.drawPoint(ctx, legendItem.pointStyle, radius, centerX, centerY);
          }
          else {
            // Draw box as legend symbol
            ctx.strokeRect(x, y, boxWidth, fontSize);
            ctx.fillRect(x, y, boxWidth, fontSize);
          }

          ctx.restore();
        };
        var fillText = function(x, y, legendItem, textWidth) {
          ctx.fillText(legendItem.text, boxWidth + (fontSize / 2) + x, y);

          if (legendItem.hidden) {
            // Strikethrough the text if hidden
            ctx.beginPath();
            ctx.lineWidth = 2;
            ctx.moveTo(boxWidth + (fontSize / 2) + x, y + (fontSize / 2));
            ctx.lineTo(boxWidth + (fontSize / 2) + x + textWidth, y + (fontSize / 2));
            ctx.stroke();
          }
        };

        // Horizontal
        var isHorizontal = me.isHorizontal();
        if (isHorizontal) {
          cursor = {
            x: me.left + ((legendWidth - lineWidths[0]) / 2),
            y: me.top + labelOpts.padding,
            line: 0
          };
        } else {
          cursor = {
            x: me.left + labelOpts.padding,
            y: me.top + labelOpts.padding,
            line: 0
          };
        }

        var itemHeight = fontSize + labelOpts.padding;
        helpers.each(me.legendItems, function(legendItem, i) {
          var textWidth = ctx.measureText(legendItem.text).width,
            width = labelOpts.usePointStyle ?
              fontSize + (fontSize / 2) + textWidth :
              boxWidth + (fontSize / 2) + textWidth,
            x = cursor.x,
            y = cursor.y;

          if (isHorizontal) {
            if (x + width >= legendWidth) {
              y = cursor.y += itemHeight;
              cursor.line++;
              x = cursor.x = me.left + ((legendWidth - lineWidths[cursor.line]) / 2);
            }
          } else {
            if (y + itemHeight > me.bottom) {
              x = cursor.x = x + me.columnWidths[cursor.line] + labelOpts.padding;
              y = cursor.y = me.top;
              cursor.line++;
            }
          }

          drawLegendBox(x, y, legendItem);

          hitboxes[i].left = x;
          hitboxes[i].top = y;

          // Fill the actual label
          fillText(x, y, legendItem, textWidth);

          if (isHorizontal) {
            cursor.x += width + (labelOpts.padding);
          } else {
            cursor.y += itemHeight;
          }

        });
      }
    },

    // Handle an event
    handleEvent: function(e) {
      var me = this;
      var position = helpers.getRelativePosition(e, me.chart.chart),
        x = position.x,
        y = position.y,
        opts = me.options;

      if (x >= me.left && x <= me.right && y >= me.top && y <= me.bottom) {
        // See if we are touching one of the dataset boxes
        var lh = me.legendHitBoxes;
        for (var i = 0; i < lh.length; ++i) {
          var hitBox = lh[i];

          if (x >= hitBox.left && x <= hitBox.left + hitBox.width && y >= hitBox.top && y <= hitBox.top + hitBox.height) {
            // Touching an element
            if (opts.onClick) {
              opts.onClick.call(me, e, me.legendItems[i]);
            }
            break;
          }
        }
      }
    }
  });

  // Register the legend plugin
  Chart.plugins.register({
    beforeInit: function(chartInstance) {
      var opts = chartInstance.options;
      var legendOpts = opts.legend;

      if (legendOpts) {
        chartInstance.legend = new Chart.Legend({
          ctx: chartInstance.chart.ctx,
          options: legendOpts,
          chart: chartInstance
        });

        Chart.layoutService.addBox(chartInstance, chartInstance.legend);
      }
    }
  });
};

},{}],30:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var noop = Chart.helpers.noop;

  /**
   * The plugin service singleton
   * @namespace Chart.plugins
   * @since 2.1.0
   */
  Chart.plugins = {
    _plugins: [],

    /**
     * Registers the given plugin(s) if not already registered.
     * @param {Array|Object} plugins plugin instance(s).
     */
    register: function(plugins) {
      var p = this._plugins;
      ([]).concat(plugins).forEach(function(plugin) {
        if (p.indexOf(plugin) === -1) {
          p.push(plugin);
        }
      });
    },

    /**
     * Unregisters the given plugin(s) only if registered.
     * @param {Array|Object} plugins plugin instance(s).
     */
    unregister: function(plugins) {
      var p = this._plugins;
      ([]).concat(plugins).forEach(function(plugin) {
        var idx = p.indexOf(plugin);
        if (idx !== -1) {
          p.splice(idx, 1);
        }
      });
    },

    /**
     * Remove all registered p^lugins.
     * @since 2.1.5
     */
    clear: function() {
      this._plugins = [];
    },

    /**
     * Returns the number of registered plugins?
     * @returns {Number}
     * @since 2.1.5
     */
    count: function() {
      return this._plugins.length;
    },

    /**
     * Returns all registered plugin intances.
     * @returns {Array} array of plugin objects.
     * @since 2.1.5
     */
    getAll: function() {
      return this._plugins;
    },

    /**
     * Calls registered plugins on the specified extension, with the given args. This
     * method immediately returns as soon as a plugin explicitly returns false. The
     * returned value can be used, for instance, to interrupt the current action.
     * @param {String} extension the name of the plugin method to call (e.g. 'beforeUpdate').
     * @param {Array} [args] extra arguments to apply to the extension call.
     * @returns {Boolean} false if any of the plugins return false, else returns true.
     */
    notify: function(extension, args) {
      var plugins = this._plugins;
      var ilen = plugins.length;
      var i, plugin;

      for (i=0; i<ilen; ++i) {
        plugin = plugins[i];
        if (typeof plugin[extension] === 'function') {
          if (plugin[extension].apply(plugin, args || []) === false) {
            return false;
          }
        }
      }

      return true;
    }
  };

  /**
   * Plugin extension methods.
   * @interface Chart.PluginBase
   * @since 2.1.0
   */
  Chart.PluginBase = Chart.Element.extend({
    // Called at start of chart init
    beforeInit: noop,

    // Called at end of chart init
    afterInit: noop,

    // Called at start of update
    beforeUpdate: noop,

    // Called at end of update
    afterUpdate: noop,

    // Called at start of draw
    beforeDraw: noop,

    // Called at end of draw
    afterDraw: noop,

    // Called during destroy
    destroy: noop
  });

  /**
   * Provided for backward compatibility, use Chart.plugins instead
   * @namespace Chart.pluginService
   * @deprecated since version 2.1.5
   * @todo remove me at version 3
   */
  Chart.pluginService = Chart.plugins;
};

},{}],31:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.scale = {
    display: true,
    position: "left",

    // grid line settings
    gridLines: {
      display: true,
      color: "rgba(0, 0, 0, 0.1)",
      lineWidth: 1,
      drawBorder: true,
      drawOnChartArea: true,
      drawTicks: true,
      tickMarkLength: 10,
      zeroLineWidth: 1,
      zeroLineColor: "rgba(0,0,0,0.25)",
      offsetGridLines: false
    },

    // scale label
    scaleLabel: {
      // actual label
      labelString: '',

      // display property
      display: false
    },

    // label settings
    ticks: {
      beginAtZero: false,
      minRotation: 0,
      maxRotation: 50,
      mirror: false,
      padding: 10,
      reverse: false,
      display: true,
      autoSkip: true,
      autoSkipPadding: 0,
      labelOffset: 0,
      // We pass through arrays to be rendered as multiline labels, we convert Others to strings here.
      callback: function(value) {
        return helpers.isArray(value) ? value : '' + value;
      }
    }
  };

  Chart.Scale = Chart.Element.extend({

    // These methods are ordered by lifecyle. Utilities then follow.
    // Any function defined here is inherited by all scale types.
    // Any function can be extended by the scale type

    beforeUpdate: function() {
      helpers.callCallback(this.options.beforeUpdate, [this]);
    },
    update: function(maxWidth, maxHeight, margins) {
      var me = this;

      // Update Lifecycle - Probably don't want to ever extend or overwrite this function ;)
      me.beforeUpdate();

      // Absorb the master measurements
      me.maxWidth = maxWidth;
      me.maxHeight = maxHeight;
      me.margins = helpers.extend({
        left: 0,
        right: 0,
        top: 0,
        bottom: 0
      }, margins);

      // Dimensions
      me.beforeSetDimensions();
      me.setDimensions();
      me.afterSetDimensions();

      // Data min/max
      me.beforeDataLimits();
      me.determineDataLimits();
      me.afterDataLimits();

      // Ticks
      me.beforeBuildTicks();
      me.buildTicks();
      me.afterBuildTicks();

      me.beforeTickToLabelConversion();
      me.convertTicksToLabels();
      me.afterTickToLabelConversion();

      // Tick Rotation
      me.beforeCalculateTickRotation();
      me.calculateTickRotation();
      me.afterCalculateTickRotation();
      // Fit
      me.beforeFit();
      me.fit();
      me.afterFit();
      //
      me.afterUpdate();

      return me.minSize;

    },
    afterUpdate: function() {
      helpers.callCallback(this.options.afterUpdate, [this]);
    },

    //

    beforeSetDimensions: function() {
      helpers.callCallback(this.options.beforeSetDimensions, [this]);
    },
    setDimensions: function() {
      var me = this;
      // Set the unconstrained dimension before label rotation
      if (me.isHorizontal()) {
        // Reset position before calculating rotation
        me.width = me.maxWidth;
        me.left = 0;
        me.right = me.width;
      } else {
        me.height = me.maxHeight;

        // Reset position before calculating rotation
        me.top = 0;
        me.bottom = me.height;
      }

      // Reset padding
      me.paddingLeft = 0;
      me.paddingTop = 0;
      me.paddingRight = 0;
      me.paddingBottom = 0;
    },
    afterSetDimensions: function() {
      helpers.callCallback(this.options.afterSetDimensions, [this]);
    },

    // Data limits
    beforeDataLimits: function() {
      helpers.callCallback(this.options.beforeDataLimits, [this]);
    },
    determineDataLimits: helpers.noop,
    afterDataLimits: function() {
      helpers.callCallback(this.options.afterDataLimits, [this]);
    },

    //
    beforeBuildTicks: function() {
      helpers.callCallback(this.options.beforeBuildTicks, [this]);
    },
    buildTicks: helpers.noop,
    afterBuildTicks: function() {
      helpers.callCallback(this.options.afterBuildTicks, [this]);
    },

    beforeTickToLabelConversion: function() {
      helpers.callCallback(this.options.beforeTickToLabelConversion, [this]);
    },
    convertTicksToLabels: function() {
      var me = this;
      // Convert ticks to strings
      me.ticks = me.ticks.map(function(numericalTick, index, ticks) {
          if (me.options.ticks.userCallback) {
            return me.options.ticks.userCallback(numericalTick, index, ticks);
          }
          return me.options.ticks.callback(numericalTick, index, ticks);
        },
        me);
    },
    afterTickToLabelConversion: function() {
      helpers.callCallback(this.options.afterTickToLabelConversion, [this]);
    },

    //

    beforeCalculateTickRotation: function() {
      helpers.callCallback(this.options.beforeCalculateTickRotation, [this]);
    },
    calculateTickRotation: function() {
      var me = this;
      var context = me.ctx;
      var globalDefaults = Chart.defaults.global;
      var optionTicks = me.options.ticks;

      //Get the width of each grid by calculating the difference
      //between x offsets between 0 and 1.
      var tickFontSize = helpers.getValueOrDefault(optionTicks.fontSize, globalDefaults.defaultFontSize);
      var tickFontStyle = helpers.getValueOrDefault(optionTicks.fontStyle, globalDefaults.defaultFontStyle);
      var tickFontFamily = helpers.getValueOrDefault(optionTicks.fontFamily, globalDefaults.defaultFontFamily);
      var tickLabelFont = helpers.fontString(tickFontSize, tickFontStyle, tickFontFamily);
      context.font = tickLabelFont;

      var firstWidth = context.measureText(me.ticks[0]).width;
      var lastWidth = context.measureText(me.ticks[me.ticks.length - 1]).width;
      var firstRotated;

      me.labelRotation = optionTicks.minRotation || 0;
      me.paddingRight = 0;
      me.paddingLeft = 0;

      if (me.options.display) {
        if (me.isHorizontal()) {
          me.paddingRight = lastWidth / 2 + 3;
          me.paddingLeft = firstWidth / 2 + 3;

          if (!me.longestTextCache) {
            me.longestTextCache = {};
          }
          var originalLabelWidth = helpers.longestText(context, tickLabelFont, me.ticks, me.longestTextCache);
          var labelWidth = originalLabelWidth;
          var cosRotation;
          var sinRotation;

          // Allow 3 pixels x2 padding either side for label readability
          // only the index matters for a dataset scale, but we want a consistent interface between scales
          var tickWidth = me.getPixelForTick(1) - me.getPixelForTick(0) - 6;

          //Max label rotation can be set or default to 90 - also act as a loop counter
          while (labelWidth > tickWidth && me.labelRotation < optionTicks.maxRotation) {
            cosRotation = Math.cos(helpers.toRadians(me.labelRotation));
            sinRotation = Math.sin(helpers.toRadians(me.labelRotation));

            firstRotated = cosRotation * firstWidth;

            // We're right aligning the text now.
            if (firstRotated + tickFontSize / 2 > me.yLabelWidth) {
              me.paddingLeft = firstRotated + tickFontSize / 2;
            }

            me.paddingRight = tickFontSize / 2;

            if (sinRotation * originalLabelWidth > me.maxHeight) {
              // go back one step
              me.labelRotation--;
              break;
            }

            me.labelRotation++;
            labelWidth = cosRotation * originalLabelWidth;
          }
        }
      }

      if (me.margins) {
        me.paddingLeft = Math.max(me.paddingLeft - me.margins.left, 0);
        me.paddingRight = Math.max(me.paddingRight - me.margins.right, 0);
      }
    },
    afterCalculateTickRotation: function() {
      helpers.callCallback(this.options.afterCalculateTickRotation, [this]);
    },

    //

    beforeFit: function() {
      helpers.callCallback(this.options.beforeFit, [this]);
    },
    fit: function() {
      var me = this;
      // Reset
      var minSize = me.minSize = {
        width: 0,
        height: 0
      };

      var opts = me.options;
      var globalDefaults = Chart.defaults.global;
      var tickOpts = opts.ticks;
      var scaleLabelOpts = opts.scaleLabel;
      var display = opts.display;
      var isHorizontal = me.isHorizontal();

      var tickFontSize = helpers.getValueOrDefault(tickOpts.fontSize, globalDefaults.defaultFontSize);
      var tickFontStyle = helpers.getValueOrDefault(tickOpts.fontStyle, globalDefaults.defaultFontStyle);
      var tickFontFamily = helpers.getValueOrDefault(tickOpts.fontFamily, globalDefaults.defaultFontFamily);
      var tickLabelFont = helpers.fontString(tickFontSize, tickFontStyle, tickFontFamily);

      var scaleLabelFontSize = helpers.getValueOrDefault(scaleLabelOpts.fontSize, globalDefaults.defaultFontSize);

      var tickMarkLength = opts.gridLines.tickMarkLength;

      // Width
      if (isHorizontal) {
        // subtract the margins to line up with the chartArea if we are a full width scale
        minSize.width = me.isFullWidth() ? me.maxWidth - me.margins.left - me.margins.right : me.maxWidth;
      } else {
        minSize.width = display ? tickMarkLength : 0;
      }

      // height
      if (isHorizontal) {
        minSize.height = display ? tickMarkLength : 0;
      } else {
        minSize.height = me.maxHeight; // fill all the height
      }

      // Are we showing a title for the scale?
      if (scaleLabelOpts.display && display) {
        if (isHorizontal) {
          minSize.height += (scaleLabelFontSize * 1.5);
        } else {
          minSize.width += (scaleLabelFontSize * 1.5);
        }
      }

      if (tickOpts.display && display) {
        // Don't bother fitting the ticks if we are not showing them
        if (!me.longestTextCache) {
          me.longestTextCache = {};
        }

        var largestTextWidth = helpers.longestText(me.ctx, tickLabelFont, me.ticks, me.longestTextCache);
        var tallestLabelHeightInLines = helpers.numberOfLabelLines(me.ticks);
        var lineSpace = tickFontSize * 0.5;

        if (isHorizontal) {
          // A horizontal axis is more constrained by the height.
          me.longestLabelWidth = largestTextWidth;

          // TODO - improve this calculation
          var labelHeight = (Math.sin(helpers.toRadians(me.labelRotation)) * me.longestLabelWidth) + (tickFontSize * tallestLabelHeightInLines) + (lineSpace * tallestLabelHeightInLines);

          minSize.height = Math.min(me.maxHeight, minSize.height + labelHeight);
          me.ctx.font = tickLabelFont;

          var firstLabelWidth = me.ctx.measureText(me.ticks[0]).width;
          var lastLabelWidth = me.ctx.measureText(me.ticks[me.ticks.length - 1]).width;

          // Ensure that our ticks are always inside the canvas. When rotated, ticks are right aligned which means that the right padding is dominated
          // by the font height
          var cosRotation = Math.cos(helpers.toRadians(me.labelRotation));
          var sinRotation = Math.sin(helpers.toRadians(me.labelRotation));
          me.paddingLeft = me.labelRotation !== 0 ? (cosRotation * firstLabelWidth) + 3 : firstLabelWidth / 2 + 3; // add 3 px to move away from canvas edges
          me.paddingRight = me.labelRotation !== 0 ? (sinRotation * (tickFontSize / 2)) + 3 : lastLabelWidth / 2 + 3; // when rotated
        } else {
          // A vertical axis is more constrained by the width. Labels are the dominant factor here, so get that length first
          var maxLabelWidth = me.maxWidth - minSize.width;

          // Account for padding
          var mirror = tickOpts.mirror;
          if (!mirror) {
            largestTextWidth += me.options.ticks.padding;
          } else {
            // If mirrored text is on the inside so don't expand
            largestTextWidth = 0;
          }

          if (largestTextWidth < maxLabelWidth) {
            // We don't need all the room
            minSize.width += largestTextWidth;
          } else {
            // Expand to max size
            minSize.width = me.maxWidth;
          }

          me.paddingTop = tickFontSize / 2;
          me.paddingBottom = tickFontSize / 2;
        }
      }

      if (me.margins) {
        me.paddingLeft = Math.max(me.paddingLeft - me.margins.left, 0);
        me.paddingTop = Math.max(me.paddingTop - me.margins.top, 0);
        me.paddingRight = Math.max(me.paddingRight - me.margins.right, 0);
        me.paddingBottom = Math.max(me.paddingBottom - me.margins.bottom, 0);
      }

      me.width = minSize.width;
      me.height = minSize.height;

    },
    afterFit: function() {
      helpers.callCallback(this.options.afterFit, [this]);
    },

    // Shared Methods
    isHorizontal: function() {
      return this.options.position === "top" || this.options.position === "bottom";
    },
    isFullWidth: function() {
      return (this.options.fullWidth);
    },

    // Get the correct value. NaN bad inputs, If the value type is object get the x or y based on whether we are horizontal or not
    getRightValue: function(rawValue) {
      // Null and undefined values first
      if (rawValue === null || typeof(rawValue) === 'undefined') {
        return NaN;
      }
      // isNaN(object) returns true, so make sure NaN is checking for a number
      if (typeof(rawValue) === 'number' && isNaN(rawValue)) {
        return NaN;
      }
      // If it is in fact an object, dive in one more level
      if (typeof(rawValue) === "object") {
        if ((rawValue instanceof Date) || (rawValue.isValid)) {
          return rawValue;
        } else {
          return this.getRightValue(this.isHorizontal() ? rawValue.x : rawValue.y);
        }
      }

      // Value is good, return it
      return rawValue;
    },

    // Used to get the value to display in the tooltip for the data at the given index
    // function getLabelForIndex(index, datasetIndex)
    getLabelForIndex: helpers.noop,

    // Used to get data value locations.  Value can either be an index or a numerical value
    getPixelForValue: helpers.noop,

    // Used to get the data value from a given pixel. This is the inverse of getPixelForValue
    getValueForPixel: helpers.noop,

    // Used for tick location, should
    getPixelForTick: function(index, includeOffset) {
      var me = this;
      if (me.isHorizontal()) {
        var innerWidth = me.width - (me.paddingLeft + me.paddingRight);
        var tickWidth = innerWidth / Math.max((me.ticks.length - ((me.options.gridLines.offsetGridLines) ? 0 : 1)), 1);
        var pixel = (tickWidth * index) + me.paddingLeft;

        if (includeOffset) {
          pixel += tickWidth / 2;
        }

        var finalVal = me.left + Math.round(pixel);
        finalVal += me.isFullWidth() ? me.margins.left : 0;
        return finalVal;
      } else {
        var innerHeight = me.height - (me.paddingTop + me.paddingBottom);
        return me.top + (index * (innerHeight / (me.ticks.length - 1)));
      }
    },

    // Utility for getting the pixel location of a percentage of scale
    getPixelForDecimal: function(decimal /*, includeOffset*/ ) {
      var me = this;
      if (me.isHorizontal()) {
        var innerWidth = me.width - (me.paddingLeft + me.paddingRight);
        var valueOffset = (innerWidth * decimal) + me.paddingLeft;

        var finalVal = me.left + Math.round(valueOffset);
        finalVal += me.isFullWidth() ? me.margins.left : 0;
        return finalVal;
      } else {
        return me.top + (decimal * me.height);
      }
    },

    getBasePixel: function() {
      var me = this;
      var min = me.min;
      var max = me.max;

      return me.getPixelForValue(
        me.beginAtZero? 0:
        min < 0 && max < 0? max :
        min > 0 && max > 0? min :
        0);
    },

    // Actualy draw the scale on the canvas
    // @param {rectangle} chartArea : the area of the chart to draw full grid lines on
    draw: function(chartArea) {
      var me = this;
      var options = me.options;
      if (!options.display) {
        return;
      }

      var context = me.ctx;
      var globalDefaults = Chart.defaults.global;
      var optionTicks = options.ticks;
      var gridLines = options.gridLines;
      var scaleLabel = options.scaleLabel;

      var isRotated = me.labelRotation !== 0;
      var skipRatio;
      var useAutoskipper = optionTicks.autoSkip;
      var isHorizontal = me.isHorizontal();

      // figure out the maximum number of gridlines to show
      var maxTicks;
      if (optionTicks.maxTicksLimit) {
        maxTicks = optionTicks.maxTicksLimit;
      }

      var tickFontColor = helpers.getValueOrDefault(optionTicks.fontColor, globalDefaults.defaultFontColor);
      var tickFontSize = helpers.getValueOrDefault(optionTicks.fontSize, globalDefaults.defaultFontSize);
      var tickFontStyle = helpers.getValueOrDefault(optionTicks.fontStyle, globalDefaults.defaultFontStyle);
      var tickFontFamily = helpers.getValueOrDefault(optionTicks.fontFamily, globalDefaults.defaultFontFamily);
      var tickLabelFont = helpers.fontString(tickFontSize, tickFontStyle, tickFontFamily);
      var tl = gridLines.tickMarkLength;

      var scaleLabelFontColor = helpers.getValueOrDefault(scaleLabel.fontColor, globalDefaults.defaultFontColor);
      var scaleLabelFontSize = helpers.getValueOrDefault(scaleLabel.fontSize, globalDefaults.defaultFontSize);
      var scaleLabelFontStyle = helpers.getValueOrDefault(scaleLabel.fontStyle, globalDefaults.defaultFontStyle);
      var scaleLabelFontFamily = helpers.getValueOrDefault(scaleLabel.fontFamily, globalDefaults.defaultFontFamily);
      var scaleLabelFont = helpers.fontString(scaleLabelFontSize, scaleLabelFontStyle, scaleLabelFontFamily);

      var labelRotationRadians = helpers.toRadians(me.labelRotation);
      var cosRotation = Math.cos(labelRotationRadians);
      var longestRotatedLabel = me.longestLabelWidth * cosRotation;

      // Make sure we draw text in the correct color and font
      context.fillStyle = tickFontColor;

      var itemsToDraw = [];

      if (isHorizontal) {
        skipRatio = false;

        // Only calculate the skip ratio with the half width of longestRotateLabel if we got an actual rotation
        // See #2584
        if (isRotated) {
          longestRotatedLabel /= 2;
        }

        if ((longestRotatedLabel + optionTicks.autoSkipPadding) * me.ticks.length > (me.width - (me.paddingLeft + me.paddingRight))) {
          skipRatio = 1 + Math.floor(((longestRotatedLabel + optionTicks.autoSkipPadding) * me.ticks.length) / (me.width - (me.paddingLeft + me.paddingRight)));
        }

        // if they defined a max number of optionTicks,
        // increase skipRatio until that number is met
        if (maxTicks && me.ticks.length > maxTicks) {
          while (!skipRatio || me.ticks.length / (skipRatio || 1) > maxTicks) {
            if (!skipRatio) {
              skipRatio = 1;
            }
            skipRatio += 1;
          }
        }

        if (!useAutoskipper) {
          skipRatio = false;
        }
      }


      var xTickStart = options.position === "right" ? me.left : me.right - tl;
      var xTickEnd = options.position === "right" ? me.left + tl : me.right;
      var yTickStart = options.position === "bottom" ? me.top : me.bottom - tl;
      var yTickEnd = options.position === "bottom" ? me.top + tl : me.bottom;

      helpers.each(me.ticks, function(label, index) {
        // If the callback returned a null or undefined value, do not draw this line
        if (label === undefined || label === null) {
          return;
        }

        var isLastTick = me.ticks.length === index + 1;

        // Since we always show the last tick,we need may need to hide the last shown one before
        var shouldSkip = (skipRatio > 1 && index % skipRatio > 0) || (index % skipRatio === 0 && index + skipRatio >= me.ticks.length);
        if (shouldSkip && !isLastTick || (label === undefined || label === null)) {
          return;
        }

        var lineWidth, lineColor;
        if (index === (typeof me.zeroLineIndex !== 'undefined' ? me.zeroLineIndex : 0)) {
          // Draw the first index specially
          lineWidth = gridLines.zeroLineWidth;
          lineColor = gridLines.zeroLineColor;
        } else  {
          lineWidth = helpers.getValueAtIndexOrDefault(gridLines.lineWidth, index);
          lineColor = helpers.getValueAtIndexOrDefault(gridLines.color, index);
        }

        // Common properties
        var tx1, ty1, tx2, ty2, x1, y1, x2, y2, labelX, labelY;
        var textAlign, textBaseline = 'middle';

        if (isHorizontal) {
          if (!isRotated) {
            textBaseline = options.position === 'top' ? 'bottom' : 'top';
          }

          textAlign = isRotated ? 'right' : 'center';

          var xLineValue = me.getPixelForTick(index) + helpers.aliasPixel(lineWidth); // xvalues for grid lines
          labelX = me.getPixelForTick(index, gridLines.offsetGridLines) + optionTicks.labelOffset; // x values for optionTicks (need to consider offsetLabel option)
          labelY = (isRotated) ? me.top + 12 : options.position === 'top' ? me.bottom - tl : me.top + tl;

          tx1 = tx2 = x1 = x2 = xLineValue;
          ty1 = yTickStart;
          ty2 = yTickEnd;
          y1 = chartArea.top;
          y2 = chartArea.bottom;
        } else {
          if (options.position === 'left') {
            if (optionTicks.mirror) {
              labelX = me.right + optionTicks.padding;
              textAlign = 'left';
            } else {
              labelX = me.right - optionTicks.padding;
              textAlign = 'right';
            }
          } else {
            // right side
            if (optionTicks.mirror) {
              labelX = me.left - optionTicks.padding;
              textAlign = 'right';
            } else {
              labelX = me.left + optionTicks.padding;
              textAlign = 'left';
            }
          }

          var yLineValue = me.getPixelForTick(index); // xvalues for grid lines
          yLineValue += helpers.aliasPixel(lineWidth);
          labelY = me.getPixelForTick(index, gridLines.offsetGridLines);

          tx1 = xTickStart;
          tx2 = xTickEnd;
          x1 = chartArea.left;
          x2 = chartArea.right;
          ty1 = ty2 = y1 = y2 = yLineValue;
        }

        itemsToDraw.push({
          tx1: tx1,
          ty1: ty1,
          tx2: tx2,
          ty2: ty2,
          x1: x1,
          y1: y1,
          x2: x2,
          y2: y2,
          labelX: labelX,
          labelY: labelY,
          glWidth: lineWidth,
          glColor: lineColor,
          rotation: -1 * labelRotationRadians,
          label: label,
          textBaseline: textBaseline,
          textAlign: textAlign
        });
      });

      // Draw all of the tick labels, tick marks, and grid lines at the correct places
      helpers.each(itemsToDraw, function(itemToDraw) {
        if (gridLines.display) {
          context.lineWidth = itemToDraw.glWidth;
          context.strokeStyle = itemToDraw.glColor;

          context.beginPath();

          if (gridLines.drawTicks) {
            context.moveTo(itemToDraw.tx1, itemToDraw.ty1);
            context.lineTo(itemToDraw.tx2, itemToDraw.ty2);
          }

          if (gridLines.drawOnChartArea) {
            context.moveTo(itemToDraw.x1, itemToDraw.y1);
            context.lineTo(itemToDraw.x2, itemToDraw.y2);
          }

          context.stroke();
        }

        if (optionTicks.display) {
          context.save();
          context.translate(itemToDraw.labelX, itemToDraw.labelY);
          context.rotate(itemToDraw.rotation);
          context.font = tickLabelFont;
          context.textBaseline = itemToDraw.textBaseline;
          context.textAlign = itemToDraw.textAlign;

          var label = itemToDraw.label;
          if (helpers.isArray(label)) {
            for (var i = 0, y = 0; i < label.length; ++i) {
              // We just make sure the multiline element is a string here..
              context.fillText('' + label[i], 0, y);
              // apply same lineSpacing as calculated @ L#320
              y += (tickFontSize * 1.5);
            }
          } else {
            context.fillText(label, 0, 0);
          }
          context.restore();
        }
      });

      if (scaleLabel.display) {
        // Draw the scale label
        var scaleLabelX;
        var scaleLabelY;
        var rotation = 0;

        if (isHorizontal) {
          scaleLabelX = me.left + ((me.right - me.left) / 2); // midpoint of the width
          scaleLabelY = options.position === 'bottom' ? me.bottom - (scaleLabelFontSize / 2) : me.top + (scaleLabelFontSize / 2);
        } else {
          var isLeft = options.position === 'left';
          scaleLabelX = isLeft ? me.left + (scaleLabelFontSize / 2) : me.right - (scaleLabelFontSize / 2);
          scaleLabelY = me.top + ((me.bottom - me.top) / 2);
          rotation = isLeft ? -0.5 * Math.PI : 0.5 * Math.PI;
        }

        context.save();
        context.translate(scaleLabelX, scaleLabelY);
        context.rotate(rotation);
        context.textAlign = 'center';
        context.textBaseline = 'middle';
        context.fillStyle = scaleLabelFontColor; // render in correct colour
        context.font = scaleLabelFont;
        context.fillText(scaleLabel.labelString, 0, 0);
        context.restore();
      }

      if (gridLines.drawBorder) {
        // Draw the line at the edge of the axis
        context.lineWidth = helpers.getValueAtIndexOrDefault(gridLines.lineWidth, 0);
        context.strokeStyle = helpers.getValueAtIndexOrDefault(gridLines.color, 0);
        var x1 = me.left,
          x2 = me.right,
          y1 = me.top,
          y2 = me.bottom;

        var aliasPixel = helpers.aliasPixel(context.lineWidth);
        if (isHorizontal) {
          y1 = y2 = options.position === 'top' ? me.bottom : me.top;
          y1 += aliasPixel;
          y2 += aliasPixel;
        } else {
          x1 = x2 = options.position === 'left' ? me.right : me.left;
          x1 += aliasPixel;
          x2 += aliasPixel;
        }

        context.beginPath();
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.stroke();
      }
    }
  });
};

},{}],32:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.scaleService = {
    // Scale registration object. Extensions can register new scale types (such as log or DB scales) and then
    // use the new chart options to grab the correct scale
    constructors: {},
    // Use a registration function so that we can move to an ES6 map when we no longer need to support
    // old browsers

    // Scale config defaults
    defaults: {},
    registerScaleType: function(type, scaleConstructor, defaults) {
      this.constructors[type] = scaleConstructor;
      this.defaults[type] = helpers.clone(defaults);
    },
    getScaleConstructor: function(type) {
      return this.constructors.hasOwnProperty(type) ? this.constructors[type] : undefined;
    },
    getScaleDefaults: function(type) {
      // Return the scale defaults merged with the global settings so that we always use the latest ones
      return this.defaults.hasOwnProperty(type) ? helpers.scaleMerge(Chart.defaults.scale, this.defaults[type]) : {};
    },
    updateScaleDefaults: function(type, additions) {
      var defaults = this.defaults;
      if (defaults.hasOwnProperty(type)) {
        defaults[type] = helpers.extend(defaults[type], additions);
      }
    },
    addScalesToLayout: function(chartInstance) {
      // Adds each scale to the chart.boxes array to be sized accordingly
      helpers.each(chartInstance.scales, function(scale) {
        Chart.layoutService.addBox(chartInstance, scale);
      });
    }
  };
};
},{}],33:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.global.title = {
    display: false,
    position: 'top',
    fullWidth: true, // marks that this box should take the full width of the canvas (pushing down other boxes)

    fontStyle: 'bold',
    padding: 10,

    // actual title
    text: ''
  };

  var noop = helpers.noop;
  Chart.Title = Chart.Element.extend({

    initialize: function(config) {
      var me = this;
      helpers.extend(me, config);
      me.options = helpers.configMerge(Chart.defaults.global.title, config.options);

      // Contains hit boxes for each dataset (in dataset order)
      me.legendHitBoxes = [];
    },

    // These methods are ordered by lifecyle. Utilities then follow.

    beforeUpdate: function () {
      var chartOpts = this.chart.options;
      if (chartOpts && chartOpts.title) {
        this.options = helpers.configMerge(Chart.defaults.global.title, chartOpts.title);
      }
    },
    update: function(maxWidth, maxHeight, margins) {
      var me = this;

      // Update Lifecycle - Probably don't want to ever extend or overwrite this function ;)
      me.beforeUpdate();

      // Absorb the master measurements
      me.maxWidth = maxWidth;
      me.maxHeight = maxHeight;
      me.margins = margins;

      // Dimensions
      me.beforeSetDimensions();
      me.setDimensions();
      me.afterSetDimensions();
      // Labels
      me.beforeBuildLabels();
      me.buildLabels();
      me.afterBuildLabels();

      // Fit
      me.beforeFit();
      me.fit();
      me.afterFit();
      //
      me.afterUpdate();

      return me.minSize;

    },
    afterUpdate: noop,

    //

    beforeSetDimensions: noop,
    setDimensions: function() {
      var me = this;
      // Set the unconstrained dimension before label rotation
      if (me.isHorizontal()) {
        // Reset position before calculating rotation
        me.width = me.maxWidth;
        me.left = 0;
        me.right = me.width;
      } else {
        me.height = me.maxHeight;

        // Reset position before calculating rotation
        me.top = 0;
        me.bottom = me.height;
      }

      // Reset padding
      me.paddingLeft = 0;
      me.paddingTop = 0;
      me.paddingRight = 0;
      me.paddingBottom = 0;

      // Reset minSize
      me.minSize = {
        width: 0,
        height: 0
      };
    },
    afterSetDimensions: noop,

    //

    beforeBuildLabels: noop,
    buildLabels: noop,
    afterBuildLabels: noop,

    //

    beforeFit: noop,
    fit: function() {
      var me = this,
        valueOrDefault = helpers.getValueOrDefault,
        opts = me.options,
        globalDefaults = Chart.defaults.global,
        display = opts.display,
        fontSize = valueOrDefault(opts.fontSize, globalDefaults.defaultFontSize),
        minSize = me.minSize;

      if (me.isHorizontal()) {
        minSize.width = me.maxWidth; // fill all the width
        minSize.height = display ? fontSize + (opts.padding * 2) : 0;
      } else {
        minSize.width = display ? fontSize + (opts.padding * 2) : 0;
        minSize.height = me.maxHeight; // fill all the height
      }

      me.width = minSize.width;
      me.height = minSize.height;

    },
    afterFit: noop,

    // Shared Methods
    isHorizontal: function() {
      var pos = this.options.position;
      return pos === "top" || pos === "bottom";
    },

    // Actualy draw the title block on the canvas
    draw: function() {
      var me = this,
        ctx = me.ctx,
        valueOrDefault = helpers.getValueOrDefault,
        opts = me.options,
        globalDefaults = Chart.defaults.global;

      if (opts.display) {
        var fontSize = valueOrDefault(opts.fontSize, globalDefaults.defaultFontSize),
          fontStyle = valueOrDefault(opts.fontStyle, globalDefaults.defaultFontStyle),
          fontFamily = valueOrDefault(opts.fontFamily, globalDefaults.defaultFontFamily),
          titleFont = helpers.fontString(fontSize, fontStyle, fontFamily),
          rotation = 0,
          titleX,
          titleY,
          top = me.top,
          left = me.left,
          bottom = me.bottom,
          right = me.right;

        ctx.fillStyle = valueOrDefault(opts.fontColor, globalDefaults.defaultFontColor); // render in correct colour
        ctx.font = titleFont;

        // Horizontal
        if (me.isHorizontal()) {
          titleX = left + ((right - left) / 2); // midpoint of the width
          titleY = top + ((bottom - top) / 2); // midpoint of the height
        } else {
          titleX = opts.position === 'left' ? left + (fontSize / 2) : right - (fontSize / 2);
          titleY = top + ((bottom - top) / 2);
          rotation = Math.PI * (opts.position === 'left' ? -0.5 : 0.5);
        }

        ctx.save();
        ctx.translate(titleX, titleY);
        ctx.rotate(rotation);
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(opts.text, 0, 0);
        ctx.restore();
      }
    }
  });

  // Register the title plugin
  Chart.plugins.register({
    beforeInit: function(chartInstance) {
      var opts = chartInstance.options;
      var titleOpts = opts.title;

      if (titleOpts) {
        chartInstance.titleBlock = new Chart.Title({
          ctx: chartInstance.chart.ctx,
          options: titleOpts,
          chart: chartInstance
        });

        Chart.layoutService.addBox(chartInstance, chartInstance.titleBlock);
      }
    }
  });
};

},{}],34:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  Chart.defaults.global.tooltips = {
    enabled: true,
    custom: null,
    mode: 'single',
    backgroundColor: "rgba(0,0,0,0.8)",
    titleFontStyle: "bold",
    titleSpacing: 2,
    titleMarginBottom: 6,
    titleFontColor: "#fff",
    titleAlign: "left",
    bodySpacing: 2,
    bodyFontColor: "#fff",
    bodyAlign: "left",
    footerFontStyle: "bold",
    footerSpacing: 2,
    footerMarginTop: 6,
    footerFontColor: "#fff",
    footerAlign: "left",
    yPadding: 6,
    xPadding: 6,
    yAlign : 'center',
    xAlign : 'center',
    caretSize: 5,
    cornerRadius: 6,
    multiKeyBackground: '#fff',
    callbacks: {
      // Args are: (tooltipItems, data)
      beforeTitle: helpers.noop,
      title: function(tooltipItems, data) {
        // Pick first xLabel for now
        var title = '';
        var labels = data.labels;
        var labelCount = labels ? labels.length : 0;

        if (tooltipItems.length > 0) {
          var item = tooltipItems[0];

          if (item.xLabel) {
            title = item.xLabel;
          } else if (labelCount > 0 && item.index < labelCount) {
            title = labels[item.index];
          }
        }

        return title;
      },
      afterTitle: helpers.noop,

      // Args are: (tooltipItems, data)
      beforeBody: helpers.noop,

      // Args are: (tooltipItem, data)
      beforeLabel: helpers.noop,
      label: function(tooltipItem, data) {
        var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
        return datasetLabel + ': ' + tooltipItem.yLabel;
      },
      labelColor: function(tooltipItem, chartInstance) {
        var meta = chartInstance.getDatasetMeta(tooltipItem.datasetIndex);
        var activeElement = meta.data[tooltipItem.index];
        var view = activeElement._view;
        return {
          borderColor: view.borderColor,
          backgroundColor: view.backgroundColor
        };
      },
      afterLabel: helpers.noop,

      // Args are: (tooltipItems, data)
      afterBody: helpers.noop,

      // Args are: (tooltipItems, data)
      beforeFooter: helpers.noop,
      footer: helpers.noop,
      afterFooter: helpers.noop
    }
  };

  // Helper to push or concat based on if the 2nd parameter is an array or not
  function pushOrConcat(base, toPush) {
    if (toPush) {
      if (helpers.isArray(toPush)) {
        //base = base.concat(toPush);
        Array.prototype.push.apply(base, toPush);
      } else {
        base.push(toPush);
      }
    }

    return base;
  }

  function getAveragePosition(elements) {
    if (!elements.length) {
      return false;
    }

    var i, len;
    var xPositions = [];
    var yPositions = [];

    for (i = 0, len = elements.length; i < len; ++i) {
      var el = elements[i];
      if (el && el.hasValue()){
        var pos = el.tooltipPosition();
        xPositions.push(pos.x);
        yPositions.push(pos.y);
      }
    }

    var x = 0,
      y = 0;
    for (i = 0; i < xPositions.length; ++i) {
      if (xPositions[ i ]) {
        x += xPositions[i];
        y += yPositions[i];
      }
    }

    return {
      x: Math.round(x / xPositions.length),
      y: Math.round(y / xPositions.length)
    };
  }

  // Private helper to create a tooltip iteam model
  // @param element : the chart element (point, arc, bar) to create the tooltip item for
  // @return : new tooltip item
  function createTooltipItem(element) {
    var xScale = element._xScale;
    var yScale = element._yScale || element._scale; // handle radar || polarArea charts
    var index = element._index,
      datasetIndex = element._datasetIndex;

    return {
      xLabel: xScale ? xScale.getLabelForIndex(index, datasetIndex) : '',
      yLabel: yScale ? yScale.getLabelForIndex(index, datasetIndex) : '',
      index: index,
      datasetIndex: datasetIndex
    };
  }

  Chart.Tooltip = Chart.Element.extend({
    initialize: function() {
      var me = this;
      var globalDefaults = Chart.defaults.global;
      var tooltipOpts = me._options;
      var getValueOrDefault = helpers.getValueOrDefault;

      helpers.extend(me, {
        _model: {
          // Positioning
          xPadding: tooltipOpts.xPadding,
          yPadding: tooltipOpts.yPadding,
          xAlign : tooltipOpts.xAlign,
          yAlign : tooltipOpts.yAlign,

          // Body
          bodyFontColor: tooltipOpts.bodyFontColor,
          _bodyFontFamily: getValueOrDefault(tooltipOpts.bodyFontFamily, globalDefaults.defaultFontFamily),
          _bodyFontStyle: getValueOrDefault(tooltipOpts.bodyFontStyle, globalDefaults.defaultFontStyle),
          _bodyAlign: tooltipOpts.bodyAlign,
          bodyFontSize: getValueOrDefault(tooltipOpts.bodyFontSize, globalDefaults.defaultFontSize),
          bodySpacing: tooltipOpts.bodySpacing,

          // Title
          titleFontColor: tooltipOpts.titleFontColor,
          _titleFontFamily: getValueOrDefault(tooltipOpts.titleFontFamily, globalDefaults.defaultFontFamily),
          _titleFontStyle: getValueOrDefault(tooltipOpts.titleFontStyle, globalDefaults.defaultFontStyle),
          titleFontSize: getValueOrDefault(tooltipOpts.titleFontSize, globalDefaults.defaultFontSize),
          _titleAlign: tooltipOpts.titleAlign,
          titleSpacing: tooltipOpts.titleSpacing,
          titleMarginBottom: tooltipOpts.titleMarginBottom,

          // Footer
          footerFontColor: tooltipOpts.footerFontColor,
          _footerFontFamily: getValueOrDefault(tooltipOpts.footerFontFamily, globalDefaults.defaultFontFamily),
          _footerFontStyle: getValueOrDefault(tooltipOpts.footerFontStyle, globalDefaults.defaultFontStyle),
          footerFontSize: getValueOrDefault(tooltipOpts.footerFontSize, globalDefaults.defaultFontSize),
          _footerAlign: tooltipOpts.footerAlign,
          footerSpacing: tooltipOpts.footerSpacing,
          footerMarginTop: tooltipOpts.footerMarginTop,

          // Appearance
          caretSize: tooltipOpts.caretSize,
          cornerRadius: tooltipOpts.cornerRadius,
          backgroundColor: tooltipOpts.backgroundColor,
          opacity: 0,
          legendColorBackground: tooltipOpts.multiKeyBackground
        }
      });
    },

    // Get the title
    // Args are: (tooltipItem, data)
    getTitle: function() {
      var me = this;
      var opts = me._options;
      var callbacks = opts.callbacks;

      var beforeTitle = callbacks.beforeTitle.apply(me, arguments),
        title = callbacks.title.apply(me, arguments),
        afterTitle = callbacks.afterTitle.apply(me, arguments);

      var lines = [];
      lines = pushOrConcat(lines, beforeTitle);
      lines = pushOrConcat(lines, title);
      lines = pushOrConcat(lines, afterTitle);

      return lines;
    },

    // Args are: (tooltipItem, data)
    getBeforeBody: function() {
      var lines = this._options.callbacks.beforeBody.apply(this, arguments);
      return helpers.isArray(lines) ? lines : lines !== undefined ? [lines] : [];
    },

    // Args are: (tooltipItem, data)
    getBody: function(tooltipItems, data) {
      var me = this;
      var callbacks = me._options.callbacks;
      var bodyItems = [];

      helpers.each(tooltipItems, function(tooltipItem) {
        var bodyItem = {
          before: [],
          lines: [],
          after: []
        };
        pushOrConcat(bodyItem.before, callbacks.beforeLabel.call(me, tooltipItem, data));
        pushOrConcat(bodyItem.lines, callbacks.label.call(me, tooltipItem, data));
        pushOrConcat(bodyItem.after, callbacks.afterLabel.call(me, tooltipItem, data));

        bodyItems.push(bodyItem);
      });

      return bodyItems;
    },

    // Args are: (tooltipItem, data)
    getAfterBody: function() {
      var lines = this._options.callbacks.afterBody.apply(this, arguments);
      return helpers.isArray(lines) ? lines : lines !== undefined ? [lines] : [];
    },

    // Get the footer and beforeFooter and afterFooter lines
    // Args are: (tooltipItem, data)
    getFooter: function() {
      var me = this;
      var callbacks = me._options.callbacks;

      var beforeFooter = callbacks.beforeFooter.apply(me, arguments);
      var footer = callbacks.footer.apply(me, arguments);
      var afterFooter = callbacks.afterFooter.apply(me, arguments);

      var lines = [];
      lines = pushOrConcat(lines, beforeFooter);
      lines = pushOrConcat(lines, footer);
      lines = pushOrConcat(lines, afterFooter);

      return lines;
    },

    update: function(changed) {
      var me = this;
      var opts = me._options;
      var model = me._model;
      var active = me._active;

      var data = me._data;
      var chartInstance = me._chartInstance;

      var i, len;

      if (active.length) {
        model.opacity = 1;

        var labelColors = [],
          tooltipPosition = getAveragePosition(active);

        var tooltipItems = [];
        for (i = 0, len = active.length; i < len; ++i) {
          tooltipItems.push(createTooltipItem(active[i]));
        }

        // If the user provided a sorting function, use it to modify the tooltip items
        if (opts.itemSort) {
          tooltipItems = tooltipItems.sort(opts.itemSort);
        }

        // If there is more than one item, show color items
        if (active.length > 1) {
          helpers.each(tooltipItems, function(tooltipItem) {
            labelColors.push(opts.callbacks.labelColor.call(me, tooltipItem, chartInstance));
          });
        }

        // Build the Text Lines
        helpers.extend(model, {
          title: me.getTitle(tooltipItems, data),
          beforeBody: me.getBeforeBody(tooltipItems, data),
          body: me.getBody(tooltipItems, data),
          afterBody: me.getAfterBody(tooltipItems, data),
          footer: me.getFooter(tooltipItems, data),
          x: Math.round(tooltipPosition.x),
          y: Math.round(tooltipPosition.y),
          caretPadding: helpers.getValueOrDefault(tooltipPosition.padding, 2),
          labelColors: labelColors
        });

        // We need to determine alignment of
        var tooltipSize = me.getTooltipSize(model);
        me.determineAlignment(tooltipSize); // Smart Tooltip placement to stay on the canvas

        helpers.extend(model, me.getBackgroundPoint(model, tooltipSize));
      } else {
        me._model.opacity = 0;
      }

      if (changed && opts.custom) {
        opts.custom.call(me, model);
      }

      return me;
    },
    getTooltipSize: function(vm) {
      var ctx = this._chart.ctx;

      var size = {
        height: vm.yPadding * 2, // Tooltip Padding
        width: 0
      };

      // Count of all lines in the body
      var body = vm.body;
      var combinedBodyLength = body.reduce(function(count, bodyItem) {
        return count + bodyItem.before.length + bodyItem.lines.length + bodyItem.after.length;
      }, 0);
      combinedBodyLength += vm.beforeBody.length + vm.afterBody.length;

      var titleLineCount = vm.title.length;
      var footerLineCount = vm.footer.length;
      var titleFontSize = vm.titleFontSize,
        bodyFontSize = vm.bodyFontSize,
        footerFontSize = vm.footerFontSize;

      size.height += titleLineCount * titleFontSize; // Title Lines
      size.height += (titleLineCount - 1) * vm.titleSpacing; // Title Line Spacing
      size.height += titleLineCount ? vm.titleMarginBottom : 0; // Title's bottom Margin
      size.height += combinedBodyLength * bodyFontSize; // Body Lines
      size.height += combinedBodyLength ? (combinedBodyLength - 1) * vm.bodySpacing : 0; // Body Line Spacing
      size.height += footerLineCount ? vm.footerMarginTop : 0; // Footer Margin
      size.height += footerLineCount * (footerFontSize); // Footer Lines
      size.height += footerLineCount ? (footerLineCount - 1) * vm.footerSpacing : 0; // Footer Line Spacing

      // Title width
      var widthPadding = 0;
      var maxLineWidth = function(line) {
        size.width = Math.max(size.width, ctx.measureText(line).width + widthPadding);
      };

      ctx.font = helpers.fontString(titleFontSize, vm._titleFontStyle, vm._titleFontFamily);
      helpers.each(vm.title, maxLineWidth);

      // Body width
      ctx.font = helpers.fontString(bodyFontSize, vm._bodyFontStyle, vm._bodyFontFamily);
      helpers.each(vm.beforeBody.concat(vm.afterBody), maxLineWidth);

      // Body lines may include some extra width due to the color box
      widthPadding = body.length > 1 ? (bodyFontSize + 2) : 0;
      helpers.each(body, function(bodyItem) {
        helpers.each(bodyItem.before, maxLineWidth);
        helpers.each(bodyItem.lines, maxLineWidth);
        helpers.each(bodyItem.after, maxLineWidth);
      });

      // Reset back to 0
      widthPadding = 0;

      // Footer width
      ctx.font = helpers.fontString(footerFontSize, vm._footerFontStyle, vm._footerFontFamily);
      helpers.each(vm.footer, maxLineWidth);

      // Add padding
      size.width += 2 * vm.xPadding;

      return size;
    },
    determineAlignment: function(size) {
      var me = this;
      var model = me._model;
      var chart = me._chart;
      var chartArea = me._chartInstance.chartArea;

      if (model.y < size.height) {
        model.yAlign = 'top';
      } else if (model.y > (chart.height - size.height)) {
        model.yAlign = 'bottom';
      }

      var lf, rf; // functions to determine left, right alignment
      var olf, orf; // functions to determine if left/right alignment causes tooltip to go outside chart
      var yf; // function to get the y alignment if the tooltip goes outside of the left or right edges
      var midX = (chartArea.left + chartArea.right) / 2;
      var midY = (chartArea.top + chartArea.bottom) / 2;

      if (model.yAlign === 'center') {
        lf = function(x) {
          return x <= midX;
        };
        rf = function(x) {
          return x > midX;
        };
      } else {
        lf = function(x) {
          return x <= (size.width / 2);
        };
        rf = function(x) {
          return x >= (chart.width - (size.width / 2));
        };
      }

      olf = function(x) {
        return x + size.width > chart.width;
      };
      orf = function(x) {
        return x - size.width < 0;
      };
      yf = function(y) {
        return y <= midY ? 'top' : 'bottom';
      };

      if (lf(model.x)) {
        model.xAlign = 'left';

        // Is tooltip too wide and goes over the right side of the chart.?
        if (olf(model.x)) {
          model.xAlign = 'center';
          model.yAlign = yf(model.y);
        }
      } else if (rf(model.x)) {
        model.xAlign = 'right';

        // Is tooltip too wide and goes outside left edge of canvas?
        if (orf(model.x)) {
          model.xAlign = 'center';
          model.yAlign = yf(model.y);
        }
      }
    },
    getBackgroundPoint: function(vm, size) {
      // Background Position
      var pt = {
        x: vm.x,
        y: vm.y
      };

      var caretSize = vm.caretSize,
        caretPadding = vm.caretPadding,
        cornerRadius = vm.cornerRadius,
        xAlign = vm.xAlign,
        yAlign = vm.yAlign,
        paddingAndSize = caretSize + caretPadding,
        radiusAndPadding = cornerRadius + caretPadding;

      if (xAlign === 'right') {
        pt.x -= size.width;
      } else if (xAlign === 'center') {
        pt.x -= (size.width / 2);
      }

      if (yAlign === 'top') {
        pt.y += paddingAndSize;
      } else if (yAlign === 'bottom') {
        pt.y -= size.height + paddingAndSize;
      } else {
        pt.y -= (size.height / 2);
      }

      if (yAlign === 'center') {
        if (xAlign === 'left') {
          pt.x += paddingAndSize;
        } else if (xAlign === 'right') {
          pt.x -= paddingAndSize;
        }
      } else {
        if (xAlign === 'left') {
          pt.x -= radiusAndPadding;
        } else if (xAlign === 'right') {
          pt.x += radiusAndPadding;
        }
      }

      return pt;
    },
    drawCaret: function(tooltipPoint, size, opacity) {
      var vm = this._view;
      var ctx = this._chart.ctx;
      var x1, x2, x3;
      var y1, y2, y3;
      var caretSize = vm.caretSize;
      var cornerRadius = vm.cornerRadius;
      var xAlign = vm.xAlign,
        yAlign = vm.yAlign;
      var ptX = tooltipPoint.x,
        ptY = tooltipPoint.y;
      var width = size.width,
        height = size.height;

      if (yAlign === 'center') {
        // Left or right side
        if (xAlign === 'left') {
          x1 = ptX;
          x2 = x1 - caretSize;
          x3 = x1;
        } else {
          x1 = ptX + width;
          x2 = x1 + caretSize;
          x3 = x1;
        }

        y2 = ptY + (height / 2);
        y1 = y2 - caretSize;
        y3 = y2 + caretSize;
      } else {
        if (xAlign === 'left') {
          x1 = ptX + cornerRadius;
          x2 = x1 + caretSize;
          x3 = x2 + caretSize;
        } else if (xAlign === 'right') {
          x1 = ptX + width - cornerRadius;
          x2 = x1 - caretSize;
          x3 = x2 - caretSize;
        } else {
          x2 = ptX + (width / 2);
          x1 = x2 - caretSize;
          x3 = x2 + caretSize;
        }

        if (yAlign === 'top') {
          y1 = ptY;
          y2 = y1 - caretSize;
          y3 = y1;
        } else {
          y1 = ptY + height;
          y2 = y1 + caretSize;
          y3 = y1;
        }
      }

      var bgColor = helpers.color(vm.backgroundColor);
      ctx.fillStyle = bgColor.alpha(opacity * bgColor.alpha()).rgbString();
      ctx.beginPath();
      ctx.moveTo(x1, y1);
      ctx.lineTo(x2, y2);
      ctx.lineTo(x3, y3);
      ctx.closePath();
      ctx.fill();
    },
    drawTitle: function(pt, vm, ctx, opacity) {
      var title = vm.title;

      if (title.length) {
        ctx.textAlign = vm._titleAlign;
        ctx.textBaseline = "top";

        var titleFontSize = vm.titleFontSize,
          titleSpacing = vm.titleSpacing;

        var titleFontColor = helpers.color(vm.titleFontColor);
        ctx.fillStyle = titleFontColor.alpha(opacity * titleFontColor.alpha()).rgbString();
        ctx.font = helpers.fontString(titleFontSize, vm._titleFontStyle, vm._titleFontFamily);

        var i, len;
        for (i = 0, len = title.length; i < len; ++i) {
          ctx.fillText(title[i], pt.x, pt.y);
          pt.y += titleFontSize + titleSpacing; // Line Height and spacing

          if (i + 1 === title.length) {
            pt.y += vm.titleMarginBottom - titleSpacing; // If Last, add margin, remove spacing
          }
        }
      }
    },
    drawBody: function(pt, vm, ctx, opacity) {
      var bodyFontSize = vm.bodyFontSize;
      var bodySpacing = vm.bodySpacing;
      var body = vm.body;

      ctx.textAlign = vm._bodyAlign;
      ctx.textBaseline = "top";

      var bodyFontColor = helpers.color(vm.bodyFontColor);
      var textColor = bodyFontColor.alpha(opacity * bodyFontColor.alpha()).rgbString();
      ctx.fillStyle = textColor;
      ctx.font = helpers.fontString(bodyFontSize, vm._bodyFontStyle, vm._bodyFontFamily);

      // Before Body
      var xLinePadding = 0;
      var fillLineOfText = function(line) {
        ctx.fillText(line, pt.x + xLinePadding, pt.y);
        pt.y += bodyFontSize + bodySpacing;
      };

      // Before body lines
      helpers.each(vm.beforeBody, fillLineOfText);

      var drawColorBoxes = body.length > 1;
      xLinePadding = drawColorBoxes ? (bodyFontSize + 2) : 0;

      // Draw body lines now
      helpers.each(body, function(bodyItem, i) {
        helpers.each(bodyItem.before, fillLineOfText);

        helpers.each(bodyItem.lines, function(line) {
          // Draw Legend-like boxes if needed
          if (drawColorBoxes) {
            // Fill a white rect so that colours merge nicely if the opacity is < 1
            ctx.fillStyle = helpers.color(vm.legendColorBackground).alpha(opacity).rgbaString();
            ctx.fillRect(pt.x, pt.y, bodyFontSize, bodyFontSize);

            // Border
            ctx.strokeStyle = helpers.color(vm.labelColors[i].borderColor).alpha(opacity).rgbaString();
            ctx.strokeRect(pt.x, pt.y, bodyFontSize, bodyFontSize);

            // Inner square
            ctx.fillStyle = helpers.color(vm.labelColors[i].backgroundColor).alpha(opacity).rgbaString();
            ctx.fillRect(pt.x + 1, pt.y + 1, bodyFontSize - 2, bodyFontSize - 2);

            ctx.fillStyle = textColor;
          }

          fillLineOfText(line);
        });

        helpers.each(bodyItem.after, fillLineOfText);
      });

      // Reset back to 0 for after body
      xLinePadding = 0;

      // After body lines
      helpers.each(vm.afterBody, fillLineOfText);
      pt.y -= bodySpacing; // Remove last body spacing
    },
    drawFooter: function(pt, vm, ctx, opacity) {
      var footer = vm.footer;

      if (footer.length) {
        pt.y += vm.footerMarginTop;

        ctx.textAlign = vm._footerAlign;
        ctx.textBaseline = "top";

        var footerFontColor = helpers.color(vm.footerFontColor);
        ctx.fillStyle = footerFontColor.alpha(opacity * footerFontColor.alpha()).rgbString();
        ctx.font = helpers.fontString(vm.footerFontSize, vm._footerFontStyle, vm._footerFontFamily);

        helpers.each(footer, function(line) {
          ctx.fillText(line, pt.x, pt.y);
          pt.y += vm.footerFontSize + vm.footerSpacing;
        });
      }
    },
    draw: function() {
      var ctx = this._chart.ctx;
      var vm = this._view;

      if (vm.opacity === 0) {
        return;
      }

      var tooltipSize = this.getTooltipSize(vm);
      var pt = {
        x: vm.x,
        y: vm.y
      };

      // IE11/Edge does not like very small opacities, so snap to 0
      var opacity = Math.abs(vm.opacity < 1e-3) ? 0 : vm.opacity;

      if (this._options.enabled) {
        // Draw Background
        var bgColor = helpers.color(vm.backgroundColor);
        ctx.fillStyle = bgColor.alpha(opacity * bgColor.alpha()).rgbString();
        helpers.drawRoundedRectangle(ctx, pt.x, pt.y, tooltipSize.width, tooltipSize.height, vm.cornerRadius);
        ctx.fill();

        // Draw Caret
        this.drawCaret(pt, tooltipSize, opacity);

        // Draw Title, Body, and Footer
        pt.x += vm.xPadding;
        pt.y += vm.yPadding;

        // Titles
        this.drawTitle(pt, vm, ctx, opacity);

        // Body
        this.drawBody(pt, vm, ctx, opacity);

        // Footer
        this.drawFooter(pt, vm, ctx, opacity);
      }
    }
  });
};

},{}],35:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers,
    globalOpts = Chart.defaults.global;

  globalOpts.elements.arc = {
    backgroundColor: globalOpts.defaultColor,
    borderColor: "#fff",
    borderWidth: 2
  };

  Chart.elements.Arc = Chart.Element.extend({
    inLabelRange: function(mouseX) {
      var vm = this._view;

      if (vm) {
        return (Math.pow(mouseX - vm.x, 2) < Math.pow(vm.radius + vm.hoverRadius, 2));
      } else {
        return false;
      }
    },
    inRange: function(chartX, chartY) {
      var vm = this._view;

      if (vm) {
        var pointRelativePosition = helpers.getAngleFromPoint(vm, {
            x: chartX,
            y: chartY
          }),
          angle = pointRelativePosition.angle,
          distance = pointRelativePosition.distance;

        //Sanitise angle range
        var startAngle = vm.startAngle;
        var endAngle = vm.endAngle;
        while (endAngle < startAngle) {
          endAngle += 2.0 * Math.PI;
        }
        while (angle > endAngle) {
          angle -= 2.0 * Math.PI;
        }
        while (angle < startAngle) {
          angle += 2.0 * Math.PI;
        }

        //Check if within the range of the open/close angle
        var betweenAngles = (angle >= startAngle && angle <= endAngle),
          withinRadius = (distance >= vm.innerRadius && distance <= vm.outerRadius);

        return (betweenAngles && withinRadius);
      } else {
        return false;
      }
    },
    tooltipPosition: function() {
      var vm = this._view;

      var centreAngle = vm.startAngle + ((vm.endAngle - vm.startAngle) / 2),
        rangeFromCentre = (vm.outerRadius - vm.innerRadius) / 2 + vm.innerRadius;
      return {
        x: vm.x + (Math.cos(centreAngle) * rangeFromCentre),
        y: vm.y + (Math.sin(centreAngle) * rangeFromCentre)
      };
    },
    draw: function() {

      var ctx = this._chart.ctx,
        vm = this._view,
        sA = vm.startAngle,
        eA = vm.endAngle;

      ctx.beginPath();

      ctx.arc(vm.x, vm.y, vm.outerRadius, sA, eA);
      ctx.arc(vm.x, vm.y, vm.innerRadius, eA, sA, true);

      ctx.closePath();
      ctx.strokeStyle = vm.borderColor;
      ctx.lineWidth = vm.borderWidth;

      ctx.fillStyle = vm.backgroundColor;

      ctx.fill();
      ctx.lineJoin = 'bevel';

      if (vm.borderWidth) {
        ctx.stroke();
      }
    }
  });
};

},{}],36:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;
  var globalDefaults = Chart.defaults.global;

  Chart.defaults.global.elements.line = {
    tension: 0.4,
    backgroundColor: globalDefaults.defaultColor,
    borderWidth: 3,
    borderColor: globalDefaults.defaultColor,
    borderCapStyle: 'butt',
    borderDash: [],
    borderDashOffset: 0.0,
    borderJoinStyle: 'miter',
    capBezierPoints: true,
    fill: true // do we fill in the area between the line and its base axis
  };

  Chart.elements.Line = Chart.Element.extend({
    lineToNextPoint: function(previousPoint, point, nextPoint, skipHandler, previousSkipHandler) {
      var me = this;
      var ctx = me._chart.ctx;
      var spanGaps = me._view ? me._view.spanGaps : false;

      if (point._view.skip && !spanGaps) {
        skipHandler.call(me, previousPoint, point, nextPoint);
      } else if (previousPoint._view.skip && !spanGaps) {
        previousSkipHandler.call(me, previousPoint, point, nextPoint);
      } else if (point._view.steppedLine === true) {
        ctx.lineTo(point._view.x, previousPoint._view.y);
        ctx.lineTo(point._view.x, point._view.y);
      } else if (point._view.tension === 0) {
        ctx.lineTo(point._view.x, point._view.y);
      } else {
        // Line between points
        ctx.bezierCurveTo(
          previousPoint._view.controlPointNextX,
          previousPoint._view.controlPointNextY,
          point._view.controlPointPreviousX,
          point._view.controlPointPreviousY,
          point._view.x,
          point._view.y
        );
      }
    },

    draw: function() {
      var me = this;

      var vm = me._view;
      var ctx = me._chart.ctx;
      var first = me._children[0];
      var last = me._children[me._children.length - 1];

      function loopBackToStart(drawLineToCenter) {
        if (!first._view.skip && !last._view.skip) {
          // Draw a bezier line from last to first
          ctx.bezierCurveTo(
            last._view.controlPointNextX,
            last._view.controlPointNextY,
            first._view.controlPointPreviousX,
            first._view.controlPointPreviousY,
            first._view.x,
            first._view.y
          );
        } else if (drawLineToCenter) {
          // Go to center
          ctx.lineTo(me._view.scaleZero.x, me._view.scaleZero.y);
        }
      }

      ctx.save();

      // If we had points and want to fill this line, do so.
      if (me._children.length > 0 && vm.fill) {
        // Draw the background first (so the border is always on top)
        ctx.beginPath();

        helpers.each(me._children, function(point, index) {
          var previous = helpers.previousItem(me._children, index);
          var next = helpers.nextItem(me._children, index);

          // First point moves to it's starting position no matter what
          if (index === 0) {
            if (me._loop) {
              ctx.moveTo(vm.scaleZero.x, vm.scaleZero.y);
            } else {
              ctx.moveTo(point._view.x, vm.scaleZero);
            }

            if (point._view.skip) {
              if (!me._loop) {
                ctx.moveTo(next._view.x, me._view.scaleZero);
              }
            } else {
              ctx.lineTo(point._view.x, point._view.y);
            }
          } else {
            me.lineToNextPoint(previous, point, next, function(previousPoint, point, nextPoint) {
              if (me._loop) {
                // Go to center
                ctx.lineTo(me._view.scaleZero.x, me._view.scaleZero.y);
              } else {
                ctx.lineTo(previousPoint._view.x, me._view.scaleZero);
                ctx.moveTo(nextPoint._view.x, me._view.scaleZero);
              }
            }, function(previousPoint, point) {
              // If we skipped the last point, draw a line to ourselves so that the fill is nice
              ctx.lineTo(point._view.x, point._view.y);
            });
          }
        }, me);

        // For radial scales, loop back around to the first point
        if (me._loop) {
          loopBackToStart(true);
        } else {
          //Round off the line by going to the base of the chart, back to the start, then fill.
          ctx.lineTo(me._children[me._children.length - 1]._view.x, vm.scaleZero);
          ctx.lineTo(me._children[0]._view.x, vm.scaleZero);
        }

        ctx.fillStyle = vm.backgroundColor || globalDefaults.defaultColor;
        ctx.closePath();
        ctx.fill();
      }

      var globalOptionLineElements = globalDefaults.elements.line;
      // Now draw the line between all the points with any borders
      ctx.lineCap = vm.borderCapStyle || globalOptionLineElements.borderCapStyle;

      // IE 9 and 10 do not support line dash
      if (ctx.setLineDash) {
        ctx.setLineDash(vm.borderDash || globalOptionLineElements.borderDash);
      }

      ctx.lineDashOffset = vm.borderDashOffset || globalOptionLineElements.borderDashOffset;
      ctx.lineJoin = vm.borderJoinStyle || globalOptionLineElements.borderJoinStyle;
      ctx.lineWidth = vm.borderWidth || globalOptionLineElements.borderWidth;
      ctx.strokeStyle = vm.borderColor || globalDefaults.defaultColor;
      ctx.beginPath();

      helpers.each(me._children, function(point, index) {
        var previous = helpers.previousItem(me._children, index);
        var next = helpers.nextItem(me._children, index);

        if (index === 0) {
          ctx.moveTo(point._view.x, point._view.y);
        } else {
          me.lineToNextPoint(previous, point, next, function(previousPoint, point, nextPoint) {
            ctx.moveTo(nextPoint._view.x, nextPoint._view.y);
          }, function(previousPoint, point) {
            // If we skipped the last point, move up to our point preventing a line from being drawn
            ctx.moveTo(point._view.x, point._view.y);
          });
        }
      }, me);

      if (me._loop && me._children.length > 0) {
        loopBackToStart();
      }

      ctx.stroke();
      ctx.restore();
    }
  });
};
},{}],37:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers,
    globalOpts = Chart.defaults.global,
    defaultColor = globalOpts.defaultColor;

  globalOpts.elements.point = {
    radius: 3,
    pointStyle: 'circle',
    backgroundColor: defaultColor,
    borderWidth: 1,
    borderColor: defaultColor,
    // Hover
    hitRadius: 1,
    hoverRadius: 4,
    hoverBorderWidth: 1
  };

  Chart.elements.Point = Chart.Element.extend({
    inRange: function(mouseX, mouseY) {
      var vm = this._view;
      return vm ? ((Math.pow(mouseX - vm.x, 2) + Math.pow(mouseY - vm.y, 2)) < Math.pow(vm.hitRadius + vm.radius, 2)) : false;
    },
    inLabelRange: function(mouseX) {
      var vm = this._view;
      return vm ? (Math.pow(mouseX - vm.x, 2) < Math.pow(vm.radius + vm.hitRadius, 2)) : false;
    },
    tooltipPosition: function() {
      var vm = this._view;
      return {
        x: vm.x,
        y: vm.y,
        padding: vm.radius + vm.borderWidth
      };
    },
    draw: function() {
      var vm = this._view;
      var ctx = this._chart.ctx;
      var pointStyle = vm.pointStyle;
      var radius = vm.radius;
      var x = vm.x;
      var y = vm.y;

      if (vm.skip) {
        return;
      }

      ctx.strokeStyle = vm.borderColor || defaultColor;
      ctx.lineWidth = helpers.getValueOrDefault(vm.borderWidth, globalOpts.elements.point.borderWidth);
      ctx.fillStyle = vm.backgroundColor || defaultColor;

      Chart.canvasHelpers.drawPoint(ctx, pointStyle, radius, x, y);
    }
  });
};

},{}],38:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var globalOpts = Chart.defaults.global;

  globalOpts.elements.rectangle = {
    backgroundColor: globalOpts.defaultColor,
    borderWidth: 0,
    borderColor: globalOpts.defaultColor,
    borderSkipped: 'bottom'
  };

  Chart.elements.Rectangle = Chart.Element.extend({
    draw: function() {
      var ctx = this._chart.ctx;
      var vm = this._view;

      var halfWidth = vm.width / 2,
        leftX = vm.x - halfWidth,
        rightX = vm.x + halfWidth,
        top = vm.base - (vm.base - vm.y),
        halfStroke = vm.borderWidth / 2;

      // Canvas doesn't allow us to stroke inside the width so we can
      // adjust the sizes to fit if we're setting a stroke on the line
      if (vm.borderWidth) {
        leftX += halfStroke;
        rightX -= halfStroke;
        top += halfStroke;
      }

      ctx.beginPath();
      ctx.fillStyle = vm.backgroundColor;
      ctx.strokeStyle = vm.borderColor;
      ctx.lineWidth = vm.borderWidth;

      // Corner points, from bottom-left to bottom-right clockwise
      // | 1 2 |
      // | 0 3 |
      var corners = [
        [leftX, vm.base],
        [leftX, top],
        [rightX, top],
        [rightX, vm.base]
      ];

      // Find first (starting) corner with fallback to 'bottom'
      var borders = ['bottom', 'left', 'top', 'right'];
      var startCorner = borders.indexOf(vm.borderSkipped, 0);
      if (startCorner === -1)
        startCorner = 0;

      function cornerAt(index) {
        return corners[(startCorner + index) % 4];
      }

      // Draw rectangle from 'startCorner'
      ctx.moveTo.apply(ctx, cornerAt(0));
      for (var i = 1; i < 4; i++)
        ctx.lineTo.apply(ctx, cornerAt(i));

      ctx.fill();
      if (vm.borderWidth) {
        ctx.stroke();
      }
    },
    height: function() {
      var vm = this._view;
      return vm.base - vm.y;
    },
    inRange: function(mouseX, mouseY) {
      var vm = this._view;
      return vm ?
          (vm.y < vm.base ?
            (mouseX >= vm.x - vm.width / 2 && mouseX <= vm.x + vm.width / 2) && (mouseY >= vm.y && mouseY <= vm.base) :
            (mouseX >= vm.x - vm.width / 2 && mouseX <= vm.x + vm.width / 2) && (mouseY >= vm.base && mouseY <= vm.y)) :
          false;
    },
    inLabelRange: function(mouseX) {
      var vm = this._view;
      return vm ? (mouseX >= vm.x - vm.width / 2 && mouseX <= vm.x + vm.width / 2) : false;
    },
    tooltipPosition: function() {
      var vm = this._view;
      return {
        x: vm.x,
        y: vm.y
      };
    }
  });

};
},{}],39:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;
  // Default config for a category scale
  var defaultConfig = {
    position: "bottom"
  };

  var DatasetScale = Chart.Scale.extend({
    /**
    * Internal function to get the correct labels. If data.xLabels or data.yLabels are defined, use tose
    * else fall back to data.labels
    * @private
    */
    getLabels: function() {
      var data = this.chart.data;
      return (this.isHorizontal() ? data.xLabels : data.yLabels) || data.labels;
    },
    // Implement this so that
    determineDataLimits: function() {
      var me = this;
      var labels = me.getLabels();
      me.minIndex = 0;
      me.maxIndex = labels.length - 1;
      var findIndex;

      if (me.options.ticks.min !== undefined) {
        // user specified min value
        findIndex = helpers.indexOf(labels, me.options.ticks.min);
        me.minIndex = findIndex !== -1 ? findIndex : me.minIndex;
      }

      if (me.options.ticks.max !== undefined) {
        // user specified max value
        findIndex = helpers.indexOf(labels, me.options.ticks.max);
        me.maxIndex = findIndex !== -1 ? findIndex : me.maxIndex;
      }

      me.min = labels[me.minIndex];
      me.max = labels[me.maxIndex];
    },

    buildTicks: function() {
      var me = this;
      var labels = me.getLabels();
      // If we are viewing some subset of labels, slice the original array
      me.ticks = (me.minIndex === 0 && me.maxIndex === labels.length - 1) ? labels : labels.slice(me.minIndex, me.maxIndex + 1);
    },

    getLabelForIndex: function(index) {
      return this.ticks[index];
    },

    // Used to get data value locations.  Value can either be an index or a numerical value
    getPixelForValue: function(value, index, datasetIndex, includeOffset) {
      var me = this;
      // 1 is added because we need the length but we have the indexes
      var offsetAmt = Math.max((me.maxIndex + 1 - me.minIndex - ((me.options.gridLines.offsetGridLines) ? 0 : 1)), 1);

      if (value !== undefined) {
        var labels = me.getLabels();
        var idx = labels.indexOf(value);
        index = idx !== -1 ? idx : index;
      }

      if (me.isHorizontal()) {
        var innerWidth = me.width - (me.paddingLeft + me.paddingRight);
        var valueWidth = innerWidth / offsetAmt;
        var widthOffset = (valueWidth * (index - me.minIndex)) + me.paddingLeft;

        if (me.options.gridLines.offsetGridLines && includeOffset) {
          widthOffset += (valueWidth / 2);
        }

        return me.left + Math.round(widthOffset);
      } else {
        var innerHeight = me.height - (me.paddingTop + me.paddingBottom);
        var valueHeight = innerHeight / offsetAmt;
        var heightOffset = (valueHeight * (index - me.minIndex)) + me.paddingTop;

        if (me.options.gridLines.offsetGridLines && includeOffset) {
          heightOffset += (valueHeight / 2);
        }

        return me.top + Math.round(heightOffset);
      }
    },
    getPixelForTick: function(index, includeOffset) {
      return this.getPixelForValue(this.ticks[index], index + this.minIndex, null, includeOffset);
    },
    getValueForPixel: function(pixel) {
      var me = this;
      var value;
      var offsetAmt = Math.max((me.ticks.length - ((me.options.gridLines.offsetGridLines) ? 0 : 1)), 1);
      var horz = me.isHorizontal();
      var innerDimension = horz ? me.width - (me.paddingLeft + me.paddingRight) : me.height - (me.paddingTop + me.paddingBottom);
      var valueDimension = innerDimension / offsetAmt;

      pixel -= horz ? me.left : me.top;

      if (me.options.gridLines.offsetGridLines) {
        pixel -= (valueDimension / 2);
      }
      pixel -= horz ? me.paddingLeft : me.paddingTop;

      if (pixel <= 0) {
        value = 0;
      } else {
        value = Math.round(pixel / valueDimension);
      }

      return value;
    },
    getBasePixel: function() {
      return this.bottom;
    }
  });

  Chart.scaleService.registerScaleType("category", DatasetScale, defaultConfig);

};
},{}],40:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  var defaultConfig = {
    position: "left",
    ticks: {
      callback: function(tickValue, index, ticks) {
        // If we have lots of ticks, don't use the ones
        var delta = ticks.length > 3 ? ticks[2] - ticks[1] : ticks[1] - ticks[0];

        // If we have a number like 2.5 as the delta, figure out how many decimal places we need
        if (Math.abs(delta) > 1) {
          if (tickValue !== Math.floor(tickValue)) {
            // not an integer
            delta = tickValue - Math.floor(tickValue);
          }
        }

        var logDelta = helpers.log10(Math.abs(delta));
        var tickString = '';

        if (tickValue !== 0) {
          var numDecimal = -1 * Math.floor(logDelta);
          numDecimal = Math.max(Math.min(numDecimal, 20), 0); // toFixed has a max of 20 decimal places
          tickString = tickValue.toFixed(numDecimal);
        } else {
          tickString = '0'; // never show decimal places for 0
        }

        return tickString;
      }
    }
  };

  var LinearScale = Chart.LinearScaleBase.extend({
    determineDataLimits: function() {
      var me = this;
      var opts = me.options;
      var chart = me.chart;
      var data = chart.data;
      var datasets = data.datasets;
      var isHorizontal = me.isHorizontal();

      function IDMatches(meta) {
        return isHorizontal ? meta.xAxisID === me.id : meta.yAxisID === me.id;
      }

      // First Calculate the range
      me.min = null;
      me.max = null;

      if (opts.stacked) {
        var valuesPerType = {};
        var hasPositiveValues = false;
        var hasNegativeValues = false;

        helpers.each(datasets, function(dataset, datasetIndex) {
          var meta = chart.getDatasetMeta(datasetIndex);
          if (valuesPerType[meta.type] === undefined) {
            valuesPerType[meta.type] = {
              positiveValues: [],
              negativeValues: []
            };
          }

          // Store these per type
          var positiveValues = valuesPerType[meta.type].positiveValues;
          var negativeValues = valuesPerType[meta.type].negativeValues;

          if (chart.isDatasetVisible(datasetIndex) && IDMatches(meta)) {
            helpers.each(dataset.data, function(rawValue, index) {
              var value = +me.getRightValue(rawValue);
              if (isNaN(value) || meta.data[index].hidden) {
                return;
              }

              positiveValues[index] = positiveValues[index] || 0;
              negativeValues[index] = negativeValues[index] || 0;

              if (opts.relativePoints) {
                positiveValues[index] = 100;
              } else {
                if (value < 0) {
                  hasNegativeValues = true;
                  negativeValues[index] += value;
                } else {
                  hasPositiveValues = true;
                  positiveValues[index] += value;
                }
              }
            });
          }
        });

        helpers.each(valuesPerType, function(valuesForType) {
          var values = valuesForType.positiveValues.concat(valuesForType.negativeValues);
          var minVal = helpers.min(values);
          var maxVal = helpers.max(values);
          me.min = me.min === null ? minVal : Math.min(me.min, minVal);
          me.max = me.max === null ? maxVal : Math.max(me.max, maxVal);
        });

      } else {
        helpers.each(datasets, function(dataset, datasetIndex) {
          var meta = chart.getDatasetMeta(datasetIndex);
          if (chart.isDatasetVisible(datasetIndex) && IDMatches(meta)) {
            helpers.each(dataset.data, function(rawValue, index) {
              var value = +me.getRightValue(rawValue);
              if (isNaN(value) || meta.data[index].hidden) {
                return;
              }

              if (me.min === null) {
                me.min = value;
              } else if (value < me.min) {
                me.min = value;
              }

              if (me.max === null) {
                me.max = value;
              } else if (value > me.max) {
                me.max = value;
              }
            });
          }
        });
      }

      // Common base implementation to handle ticks.min, ticks.max, ticks.beginAtZero
      this.handleTickRangeOptions();
    },
    getTickLimit: function() {
      var maxTicks;
      var me = this;
      var tickOpts = me.options.ticks;

      if (me.isHorizontal()) {
        maxTicks = Math.min(tickOpts.maxTicksLimit ? tickOpts.maxTicksLimit : 11, Math.ceil(me.width / 50));
      } else {
        // The factor of 2 used to scale the font size has been experimentally determined.
        var tickFontSize = helpers.getValueOrDefault(tickOpts.fontSize, Chart.defaults.global.defaultFontSize);
        maxTicks = Math.min(tickOpts.maxTicksLimit ? tickOpts.maxTicksLimit : 11, Math.ceil(me.height / (2 * tickFontSize)));
      }

      return maxTicks;
    },
    // Called after the ticks are built. We need
    handleDirectionalChanges: function() {
      if (!this.isHorizontal()) {
        // We are in a vertical orientation. The top value is the highest. So reverse the array
        this.ticks.reverse();
      }
    },
    getLabelForIndex: function(index, datasetIndex) {
      return +this.getRightValue(this.chart.data.datasets[datasetIndex].data[index]);
    },
    // Utils
    getPixelForValue: function(value) {
      // This must be called after fit has been run so that
      // this.left, this.top, this.right, and this.bottom have been defined
      var me = this;
      var paddingLeft = me.paddingLeft;
      var paddingBottom = me.paddingBottom;
      var start = me.start;

      var rightValue = +me.getRightValue(value);
      var pixel;
      var innerDimension;
      var range = me.end - start;

      if (me.isHorizontal()) {
        innerDimension = me.width - (paddingLeft + me.paddingRight);
        pixel = me.left + (innerDimension / range * (rightValue - start));
        return Math.round(pixel + paddingLeft);
      } else {
        innerDimension = me.height - (me.paddingTop + paddingBottom);
        pixel = (me.bottom - paddingBottom) - (innerDimension / range * (rightValue - start));
        return Math.round(pixel);
      }
    },
    getValueForPixel: function(pixel) {
      var me = this;
      var isHorizontal = me.isHorizontal();
      var paddingLeft = me.paddingLeft;
      var paddingBottom = me.paddingBottom;
      var innerDimension = isHorizontal ? me.width - (paddingLeft + me.paddingRight) : me.height - (me.paddingTop + paddingBottom);
      var offset = (isHorizontal ? pixel - me.left - paddingLeft : me.bottom - paddingBottom - pixel) / innerDimension;
      return me.start + ((me.end - me.start) * offset);
    },
    getPixelForTick: function(index) {
      return this.getPixelForValue(this.ticksAsNumbers[index]);
    }
  });
  Chart.scaleService.registerScaleType("linear", LinearScale, defaultConfig);

};
},{}],41:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers,
    noop = helpers.noop;

  Chart.LinearScaleBase = Chart.Scale.extend({
    handleTickRangeOptions: function() {
      var me = this;
      var opts = me.options;
      var tickOpts = opts.ticks;

      // If we are forcing it to begin at 0, but 0 will already be rendered on the chart,
      // do nothing since that would make the chart weird. If the user really wants a weird chart
      // axis, they can manually override it
      if (tickOpts.beginAtZero) {
        var minSign = helpers.sign(me.min);
        var maxSign = helpers.sign(me.max);

        if (minSign < 0 && maxSign < 0) {
          // move the top up to 0
          me.max = 0;
        } else if (minSign > 0 && maxSign > 0) {
          // move the botttom down to 0
          me.min = 0;
        }
      }

      if (tickOpts.min !== undefined) {
        me.min = tickOpts.min;
      } else if (tickOpts.suggestedMin !== undefined) {
        me.min = Math.min(me.min, tickOpts.suggestedMin);
      }

      if (tickOpts.max !== undefined) {
        me.max = tickOpts.max;
      } else if (tickOpts.suggestedMax !== undefined) {
        me.max = Math.max(me.max, tickOpts.suggestedMax);
      }

      if (me.min === me.max) {
        me.max++;

        if (!tickOpts.beginAtZero) {
          me.min--;
        }
      }
    },
    getTickLimit: noop,
    handleDirectionalChanges: noop,

    buildTicks: function() {
      var me = this;
      var opts = me.options;
      var ticks = me.ticks = [];
      var tickOpts = opts.ticks;
      var getValueOrDefault = helpers.getValueOrDefault;

      // Figure out what the max number of ticks we can support it is based on the size of
      // the axis area. For now, we say that the minimum tick spacing in pixels must be 50
      // We also limit the maximum number of ticks to 11 which gives a nice 10 squares on
      // the graph

      var maxTicks = me.getTickLimit();

      // Make sure we always have at least 2 ticks
      maxTicks = Math.max(2, maxTicks);

      // To get a "nice" value for the tick spacing, we will use the appropriately named
      // "nice number" algorithm. See http://stackoverflow.com/questions/8506881/nice-label-algorithm-for-charts-with-minimum-ticks
      // for details.

      var spacing;
      var fixedStepSizeSet = (tickOpts.fixedStepSize && tickOpts.fixedStepSize > 0) || (tickOpts.stepSize && tickOpts.stepSize > 0);
      if (fixedStepSizeSet) {
        spacing = getValueOrDefault(tickOpts.fixedStepSize, tickOpts.stepSize);
      } else {
        var niceRange = helpers.niceNum(me.max - me.min, false);
        spacing = helpers.niceNum(niceRange / (maxTicks - 1), true);
      }
      var niceMin = Math.floor(me.min / spacing) * spacing;
      var niceMax = Math.ceil(me.max / spacing) * spacing;
      var numSpaces = (niceMax - niceMin) / spacing;

      // If very close to our rounded value, use it.
      if (helpers.almostEquals(numSpaces, Math.round(numSpaces), spacing / 1000)) {
        numSpaces = Math.round(numSpaces);
      } else {
        numSpaces = Math.ceil(numSpaces);
      }

      // Put the values into the ticks array
      ticks.push(tickOpts.min !== undefined ? tickOpts.min : niceMin);
      for (var j = 1; j < numSpaces; ++j) {
        ticks.push(niceMin + (j * spacing));
      }
      ticks.push(tickOpts.max !== undefined ? tickOpts.max : niceMax);

      me.handleDirectionalChanges();

      // At this point, we need to update our max and min given the tick values since we have expanded the
      // range of the scale
      me.max = helpers.max(ticks);
      me.min = helpers.min(ticks);

      if (tickOpts.reverse) {
        ticks.reverse();

        me.start = me.max;
        me.end = me.min;
      } else {
        me.start = me.min;
        me.end = me.max;
      }
    },
    convertTicksToLabels: function() {
      var me = this;
      me.ticksAsNumbers = me.ticks.slice();
      me.zeroLineIndex = me.ticks.indexOf(0);

      Chart.Scale.prototype.convertTicksToLabels.call(me);
    },
  });
};
},{}],42:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;

  var defaultConfig = {
    position: "left",

    // label settings
    ticks: {
      callback: function(value, index, arr) {
        var remain = value / (Math.pow(10, Math.floor(helpers.log10(value))));

        if (remain === 1 || remain === 2 || remain === 5 || index === 0 || index === arr.length - 1) {
          return value.toExponential();
        } else {
          return '';
        }
      }
    }
  };

  var LogarithmicScale = Chart.Scale.extend({
    determineDataLimits: function() {
      var me = this;
      var opts = me.options;
      var tickOpts = opts.ticks;
      var chart = me.chart;
      var data = chart.data;
      var datasets = data.datasets;
      var getValueOrDefault = helpers.getValueOrDefault;
      var isHorizontal = me.isHorizontal();
      function IDMatches(meta) {
        return isHorizontal ? meta.xAxisID === me.id : meta.yAxisID === me.id;
      }

      // Calculate Range
      me.min = null;
      me.max = null;

      if (opts.stacked) {
        var valuesPerType = {};

        helpers.each(datasets, function(dataset, datasetIndex) {
          var meta = chart.getDatasetMeta(datasetIndex);
          if (chart.isDatasetVisible(datasetIndex) && IDMatches(meta)) {
            if (valuesPerType[meta.type] === undefined) {
              valuesPerType[meta.type] = [];
            }

            helpers.each(dataset.data, function(rawValue, index) {
              var values = valuesPerType[meta.type];
              var value = +me.getRightValue(rawValue);
              if (isNaN(value) || meta.data[index].hidden) {
                return;
              }

              values[index] = values[index] || 0;

              if (opts.relativePoints) {
                values[index] = 100;
              } else {
                // Don't need to split positive and negative since the log scale can't handle a 0 crossing
                values[index] += value;
              }
            });
          }
        });

        helpers.each(valuesPerType, function(valuesForType) {
          var minVal = helpers.min(valuesForType);
          var maxVal = helpers.max(valuesForType);
          me.min = me.min === null ? minVal : Math.min(me.min, minVal);
          me.max = me.max === null ? maxVal : Math.max(me.max, maxVal);
        });

      } else {
        helpers.each(datasets, function(dataset, datasetIndex) {
          var meta = chart.getDatasetMeta(datasetIndex);
          if (chart.isDatasetVisible(datasetIndex) && IDMatches(meta)) {
            helpers.each(dataset.data, function(rawValue, index) {
              var value = +me.getRightValue(rawValue);
              if (isNaN(value) || meta.data[index].hidden) {
                return;
              }

              if (me.min === null) {
                me.min = value;
              } else if (value < me.min) {
                me.min = value;
              }

              if (me.max === null) {
                me.max = value;
              } else if (value > me.max) {
                me.max = value;
              }
            });
          }
        });
      }

      me.min = getValueOrDefault(tickOpts.min, me.min);
      me.max = getValueOrDefault(tickOpts.max, me.max);

      if (me.min === me.max) {
        if (me.min !== 0 && me.min !== null) {
          me.min = Math.pow(10, Math.floor(helpers.log10(me.min)) - 1);
          me.max = Math.pow(10, Math.floor(helpers.log10(me.max)) + 1);
        } else {
          me.min = 1;
          me.max = 10;
        }
      }
    },
    buildTicks: function() {
      var me = this;
      var opts = me.options;
      var tickOpts = opts.ticks;
      var getValueOrDefault = helpers.getValueOrDefault;

      // Reset the ticks array. Later on, we will draw a grid line at these positions
      // The array simply contains the numerical value of the spots where ticks will be
      var ticks = me.ticks = [];

      // Figure out what the max number of ticks we can support it is based on the size of
      // the axis area. For now, we say that the minimum tick spacing in pixels must be 50
      // We also limit the maximum number of ticks to 11 which gives a nice 10 squares on
      // the graph

      var tickVal = getValueOrDefault(tickOpts.min, Math.pow(10, Math.floor(helpers.log10(me.min))));

      while (tickVal < me.max) {
        ticks.push(tickVal);

        var exp = Math.floor(helpers.log10(tickVal));
        var significand = Math.floor(tickVal / Math.pow(10, exp)) + 1;

        if (significand === 10) {
          significand = 1;
          ++exp;
        }

        tickVal = significand * Math.pow(10, exp);
      }

      var lastTick = getValueOrDefault(tickOpts.max, tickVal);
      ticks.push(lastTick);

      if (!me.isHorizontal()) {
        // We are in a vertical orientation. The top value is the highest. So reverse the array
        ticks.reverse();
      }

      // At this point, we need to update our max and min given the tick values since we have expanded the
      // range of the scale
      me.max = helpers.max(ticks);
      me.min = helpers.min(ticks);

      if (tickOpts.reverse) {
        ticks.reverse();

        me.start = me.max;
        me.end = me.min;
      } else {
        me.start = me.min;
        me.end = me.max;
      }
    },
    convertTicksToLabels: function() {
      this.tickValues = this.ticks.slice();

      Chart.Scale.prototype.convertTicksToLabels.call(this);
    },
    // Get the correct tooltip label
    getLabelForIndex: function(index, datasetIndex) {
      return +this.getRightValue(this.chart.data.datasets[datasetIndex].data[index]);
    },
    getPixelForTick: function(index) {
      return this.getPixelForValue(this.tickValues[index]);
    },
    getPixelForValue: function(value) {
      var me = this;
      var innerDimension;
      var pixel;

      var start = me.start;
      var newVal = +me.getRightValue(value);
      var range = helpers.log10(me.end) - helpers.log10(start);
      var paddingTop = me.paddingTop;
      var paddingBottom = me.paddingBottom;
      var paddingLeft = me.paddingLeft;

      if (me.isHorizontal()) {

        if (newVal === 0) {
          pixel = me.left + paddingLeft;
        } else {
          innerDimension = me.width - (paddingLeft + me.paddingRight);
          pixel = me.left + (innerDimension / range * (helpers.log10(newVal) - helpers.log10(start)));
          pixel += paddingLeft;
        }
      } else {
        // Bottom - top since pixels increase downard on a screen
        if (newVal === 0) {
          pixel = me.top + paddingTop;
        } else {
          innerDimension = me.height - (paddingTop + paddingBottom);
          pixel = (me.bottom - paddingBottom) - (innerDimension / range * (helpers.log10(newVal) - helpers.log10(start)));
        }
      }

      return pixel;
    },
    getValueForPixel: function(pixel) {
      var me = this;
      var range = helpers.log10(me.end) - helpers.log10(me.start);
      var value, innerDimension;

      if (me.isHorizontal()) {
        innerDimension = me.width - (me.paddingLeft + me.paddingRight);
        value = me.start * Math.pow(10, (pixel - me.left - me.paddingLeft) * range / innerDimension);
      } else {
        innerDimension = me.height - (me.paddingTop + me.paddingBottom);
        value = Math.pow(10, (me.bottom - me.paddingBottom - pixel) * range / innerDimension) / me.start;
      }

      return value;
    }
  });
  Chart.scaleService.registerScaleType("logarithmic", LogarithmicScale, defaultConfig);

};
},{}],43:[function(require,module,exports){
"use strict";

module.exports = function(Chart) {

  var helpers = Chart.helpers;
  var globalDefaults = Chart.defaults.global;

  var defaultConfig = {
    display: true,

    //Boolean - Whether to animate scaling the chart from the centre
    animate: true,
    lineArc: false,
    position: "chartArea",

    angleLines: {
      display: true,
      color: "rgba(0, 0, 0, 0.1)",
      lineWidth: 1
    },

    // label settings
    ticks: {
      //Boolean - Show a backdrop to the scale label
      showLabelBackdrop: true,

      //String - The colour of the label backdrop
      backdropColor: "rgba(255,255,255,0.75)",

      //Number - The backdrop padding above & below the label in pixels
      backdropPaddingY: 2,

      //Number - The backdrop padding to the side of the label in pixels
      backdropPaddingX: 2
    },

    pointLabels: {
      //Number - Point label font size in pixels
      fontSize: 10,

      //Function - Used to convert point labels
      callback: function(label) {
        return label;
      }
    }
  };

  var LinearRadialScale = Chart.LinearScaleBase.extend({
    getValueCount: function() {
      return this.chart.data.labels.length;
    },
    setDimensions: function() {
      var me = this;
      var opts = me.options;
      var tickOpts = opts.ticks;
      // Set the unconstrained dimension before label rotation
      me.width = me.maxWidth;
      me.height = me.maxHeight;
      me.xCenter = Math.round(me.width / 2);
      me.yCenter = Math.round(me.height / 2);

      var minSize = helpers.min([me.height, me.width]);
      var tickFontSize = helpers.getValueOrDefault(tickOpts.fontSize, globalDefaults.defaultFontSize);
      me.drawingArea = opts.display ? (minSize / 2) - (tickFontSize / 2 + tickOpts.backdropPaddingY) : (minSize / 2);
    },
    determineDataLimits: function() {
      var me = this;
      var chart = me.chart;
      me.min = null;
      me.max = null;


      helpers.each(chart.data.datasets, function(dataset, datasetIndex) {
        if (chart.isDatasetVisible(datasetIndex)) {
          var meta = chart.getDatasetMeta(datasetIndex);

          helpers.each(dataset.data, function(rawValue, index) {
            var value = +me.getRightValue(rawValue);
            if (isNaN(value) || meta.data[index].hidden) {
              return;
            }

            if (me.min === null) {
              me.min = value;
            } else if (value < me.min) {
              me.min = value;
            }

            if (me.max === null) {
              me.max = value;
            } else if (value > me.max) {
              me.max = value;
            }
          });
        }
      });

      // Common base implementation to handle ticks.min, ticks.max, ticks.beginAtZero
      me.handleTickRangeOptions();
    },
    getTickLimit: function() {
      var tickOpts = this.options.ticks;
      var tickFontSize = helpers.getValueOrDefault(tickOpts.fontSize, globalDefaults.defaultFontSize);
      return Math.min(tickOpts.maxTicksLimit ? tickOpts.maxTicksLimit : 11, Math.ceil(this.drawingArea / (1.5 * tickFontSize)));
    },
    convertTicksToLabels: function() {
      var me = this;
      Chart.LinearScaleBase.prototype.convertTicksToLabels.call(me);

      // Point labels
      me.pointLabels = me.chart.data.labels.map(me.options.pointLabels.callback, me);
    },
    getLabelForIndex: function(index, datasetIndex) {
      return +this.getRightValue(this.chart.data.datasets[datasetIndex].data[index]);
    },
    fit: function() {
      /*
       * Right, this is really confusing and there is a lot of maths going on here
       * The gist of the problem is here: https://gist.github.com/nnnick/696cc9c55f4b0beb8fe9
       *
       * Reaction: https://dl.dropboxusercontent.com/u/34601363/toomuchscience.gif
       *
       * Solution:
       *
       * We assume the radius of the polygon is half the size of the canvas at first
       * at each index we check if the text overlaps.
       *
       * Where it does, we store that angle and that index.
       *
       * After finding the largest index and angle we calculate how much we need to remove
       * from the shape radius to move the point inwards by that x.
       *
       * We average the left and right distances to get the maximum shape radius that can fit in the box
       * along with labels.
       *
       * Once we have that, we can find the centre point for the chart, by taking the x text protrusion
       * on each side, removing that from the size, halving it and adding the left x protrusion width.
       *
       * This will mean we have a shape fitted to the canvas, as large as it can be with the labels
       * and position it in the most space efficient manner
       *
       * https://dl.dropboxusercontent.com/u/34601363/yeahscience.gif
       */

      var pointLabels = this.options.pointLabels;
      var pointLabelFontSize = helpers.getValueOrDefault(pointLabels.fontSize, globalDefaults.defaultFontSize);
      var pointLabeFontStyle = helpers.getValueOrDefault(pointLabels.fontStyle, globalDefaults.defaultFontStyle);
      var pointLabeFontFamily = helpers.getValueOrDefault(pointLabels.fontFamily, globalDefaults.defaultFontFamily);
      var pointLabeFont = helpers.fontString(pointLabelFontSize, pointLabeFontStyle, pointLabeFontFamily);

      // Get maximum radius of the polygon. Either half the height (minus the text width) or half the width.
      // Use this to calculate the offset + change. - Make sure L/R protrusion is at least 0 to stop issues with centre points
      var largestPossibleRadius = helpers.min([(this.height / 2 - pointLabelFontSize - 5), this.width / 2]),
        pointPosition,
        i,
        textWidth,
        halfTextWidth,
        furthestRight = this.width,
        furthestRightIndex,
        furthestRightAngle,
        furthestLeft = 0,
        furthestLeftIndex,
        furthestLeftAngle,
        xProtrusionLeft,
        xProtrusionRight,
        radiusReductionRight,
        radiusReductionLeft;
      this.ctx.font = pointLabeFont;

      for (i = 0; i < this.getValueCount(); i++) {
        // 5px to space the text slightly out - similar to what we do in the draw function.
        pointPosition = this.getPointPosition(i, largestPossibleRadius);
        textWidth = this.ctx.measureText(this.pointLabels[i] ? this.pointLabels[i] : '').width + 5;

        // Add quarter circle to make degree 0 mean top of circle
        var angleRadians = this.getIndexAngle(i) + (Math.PI / 2);
        var angle = (angleRadians * 360 / (2 * Math.PI)) % 360;

        if (angle === 0 || angle === 180) {
          // At angle 0 and 180, we're at exactly the top/bottom
          // of the radar chart, so text will be aligned centrally, so we'll half it and compare
          // w/left and right text sizes
          halfTextWidth = textWidth / 2;
          if (pointPosition.x + halfTextWidth > furthestRight) {
            furthestRight = pointPosition.x + halfTextWidth;
            furthestRightIndex = i;
          }
          if (pointPosition.x - halfTextWidth < furthestLeft) {
            furthestLeft = pointPosition.x - halfTextWidth;
            furthestLeftIndex = i;
          }
        } else if (angle < 180) {
          // Less than half the values means we'll left align the text
          if (pointPosition.x + textWidth > furthestRight) {
            furthestRight = pointPosition.x + textWidth;
            furthestRightIndex = i;
          }
        } else {
          // More than half the values means we'll right align the text
          if (pointPosition.x - textWidth < furthestLeft) {
            furthestLeft = pointPosition.x - textWidth;
            furthestLeftIndex = i;
          }
        }
      }

      xProtrusionLeft = furthestLeft;
      xProtrusionRight = Math.ceil(furthestRight - this.width);

      furthestRightAngle = this.getIndexAngle(furthestRightIndex);
      furthestLeftAngle = this.getIndexAngle(furthestLeftIndex);

      radiusReductionRight = xProtrusionRight / Math.sin(furthestRightAngle + Math.PI / 2);
      radiusReductionLeft = xProtrusionLeft / Math.sin(furthestLeftAngle + Math.PI / 2);

      // Ensure we actually need to reduce the size of the chart
      radiusReductionRight = (helpers.isNumber(radiusReductionRight)) ? radiusReductionRight : 0;
      radiusReductionLeft = (helpers.isNumber(radiusReductionLeft)) ? radiusReductionLeft : 0;

      this.drawingArea = Math.round(largestPossibleRadius - (radiusReductionLeft + radiusReductionRight) / 2);
      this.setCenterPoint(radiusReductionLeft, radiusReductionRight);
    },
    setCenterPoint: function(leftMovement, rightMovement) {
      var me = this;
      var maxRight = me.width - rightMovement - me.drawingArea,
        maxLeft = leftMovement + me.drawingArea;

      me.xCenter = Math.round(((maxLeft + maxRight) / 2) + me.left);
      // Always vertically in the centre as the text height doesn't change
      me.yCenter = Math.round((me.height / 2) + me.top);
    },

    getIndexAngle: function(index) {
      var angleMultiplier = (Math.PI * 2) / this.getValueCount();
      var startAngle = this.chart.options && this.chart.options.startAngle ?
        this.chart.options.startAngle :
        0;

      var startAngleRadians = startAngle * Math.PI * 2 / 360;

      // Start from the top instead of right, so remove a quarter of the circle
      return index * angleMultiplier - (Math.PI / 2) + startAngleRadians;
    },
    getDistanceFromCenterForValue: function(value) {
      var me = this;

      if (value === null) {
        return 0; // null always in center
      }

      // Take into account half font size + the yPadding of the top value
      var scalingFactor = me.drawingArea / (me.max - me.min);
      if (me.options.reverse) {
        return (me.max - value) * scalingFactor;
      } else {
        return (value - me.min) * scalingFactor;
      }
    },
    getPointPosition: function(index, distanceFromCenter) {
      var me = this;
      var thisAngle = me.getIndexAngle(index);
      return {
        x: Math.round(Math.cos(thisAngle) * distanceFromCenter) + me.xCenter,
        y: Math.round(Math.sin(thisAngle) * distanceFromCenter) + me.yCenter
      };
    },
    getPointPositionForValue: function(index, value) {
      return this.getPointPosition(index, this.getDistanceFromCenterForValue(value));
    },

    getBasePosition: function() {
      var me = this;
      var min = me.min;
      var max = me.max;

      return me.getPointPositionForValue(0,
        me.beginAtZero? 0:
        min < 0 && max < 0? max :
        min > 0 && max > 0? min :
        0);
    },

    draw: function() {
      var me = this;
      var opts = me.options;
      var gridLineOpts = opts.gridLines;
      var tickOpts = opts.ticks;
      var angleLineOpts = opts.angleLines;
      var pointLabelOpts = opts.pointLabels;
      var getValueOrDefault = helpers.getValueOrDefault;

      if (opts.display) {
        var ctx = me.ctx;

        // Tick Font
        var tickFontSize = getValueOrDefault(tickOpts.fontSize, globalDefaults.defaultFontSize);
        var tickFontStyle = getValueOrDefault(tickOpts.fontStyle, globalDefaults.defaultFontStyle);
        var tickFontFamily = getValueOrDefault(tickOpts.fontFamily, globalDefaults.defaultFontFamily);
        var tickLabelFont = helpers.fontString(tickFontSize, tickFontStyle, tickFontFamily);

        helpers.each(me.ticks, function(label, index) {
          // Don't draw a centre value (if it is minimum)
          if (index > 0 || opts.reverse) {
            var yCenterOffset = me.getDistanceFromCenterForValue(me.ticksAsNumbers[index]);
            var yHeight = me.yCenter - yCenterOffset;

            // Draw circular lines around the scale
            if (gridLineOpts.display && index !== 0) {
              ctx.strokeStyle = helpers.getValueAtIndexOrDefault(gridLineOpts.color, index - 1);
              ctx.lineWidth = helpers.getValueAtIndexOrDefault(gridLineOpts.lineWidth, index - 1);

              if (opts.lineArc) {
                // Draw circular arcs between the points
                ctx.beginPath();
                ctx.arc(me.xCenter, me.yCenter, yCenterOffset, 0, Math.PI * 2);
                ctx.closePath();
                ctx.stroke();
              } else {
                // Draw straight lines connecting each index
                ctx.beginPath();
                for (var i = 0; i < me.getValueCount(); i++) {
                  var pointPosition = me.getPointPosition(i, yCenterOffset);
                  if (i === 0) {
                    ctx.moveTo(pointPosition.x, pointPosition.y);
                  } else {
                    ctx.lineTo(pointPosition.x, pointPosition.y);
                  }
                }
                ctx.closePath();
                ctx.stroke();
              }
            }

            if (tickOpts.display) {
              var tickFontColor = getValueOrDefault(tickOpts.fontColor, globalDefaults.defaultFontColor);
              ctx.font = tickLabelFont;

              if (tickOpts.showLabelBackdrop) {
                var labelWidth = ctx.measureText(label).width;
                ctx.fillStyle = tickOpts.backdropColor;
                ctx.fillRect(
                  me.xCenter - labelWidth / 2 - tickOpts.backdropPaddingX,
                  yHeight - tickFontSize / 2 - tickOpts.backdropPaddingY,
                  labelWidth + tickOpts.backdropPaddingX * 2,
                  tickFontSize + tickOpts.backdropPaddingY * 2
                );
              }

              ctx.textAlign = 'center';
              ctx.textBaseline = "middle";
              ctx.fillStyle = tickFontColor;
              ctx.fillText(label, me.xCenter, yHeight);
            }
          }
        });

        if (!opts.lineArc) {
          ctx.lineWidth = angleLineOpts.lineWidth;
          ctx.strokeStyle = angleLineOpts.color;

          var outerDistance = me.getDistanceFromCenterForValue(opts.reverse ? me.min : me.max);

          // Point Label Font
          var pointLabelFontSize = getValueOrDefault(pointLabelOpts.fontSize, globalDefaults.defaultFontSize);
          var pointLabeFontStyle = getValueOrDefault(pointLabelOpts.fontStyle, globalDefaults.defaultFontStyle);
          var pointLabeFontFamily = getValueOrDefault(pointLabelOpts.fontFamily, globalDefaults.defaultFontFamily);
          var pointLabeFont = helpers.fontString(pointLabelFontSize, pointLabeFontStyle, pointLabeFontFamily);

          for (var i = me.getValueCount() - 1; i >= 0; i--) {
            if (angleLineOpts.display) {
              var outerPosition = me.getPointPosition(i, outerDistance);
              ctx.beginPath();
              ctx.moveTo(me.xCenter, me.yCenter);
              ctx.lineTo(outerPosition.x, outerPosition.y);
              ctx.stroke();
              ctx.closePath();
            }
            // Extra 3px out for some label spacing
            var pointLabelPosition = me.getPointPosition(i, outerDistance + 5);

            // Keep this in loop since we may support array properties here
            var pointLabelFontColor = getValueOrDefault(pointLabelOpts.fontColor, globalDefaults.defaultFontColor);
            ctx.font = pointLabeFont;
            ctx.fillStyle = pointLabelFontColor;

            var pointLabels = me.pointLabels;

            // Add quarter circle to make degree 0 mean top of circle
            var angleRadians = this.getIndexAngle(i) + (Math.PI / 2);
            var angle = (angleRadians * 360 / (2 * Math.PI)) % 360;

            if (angle === 0 || angle === 180) {
              ctx.textAlign = 'center';
            } else if (angle < 180) {
              ctx.textAlign = 'left';
            } else {
              ctx.textAlign = 'right';
            }

            // Set the correct text baseline based on outer positioning
            if (angle === 90 || angle === 270) {
              ctx.textBaseline = 'middle';
            } else if (angle > 270 || angle < 90) {
              ctx.textBaseline = 'bottom';
            } else {
              ctx.textBaseline = 'top';
            }

            ctx.fillText(pointLabels[i] ? pointLabels[i] : '', pointLabelPosition.x, pointLabelPosition.y);
          }
        }
      }
    }
  });
  Chart.scaleService.registerScaleType("radialLinear", LinearRadialScale, defaultConfig);

};

},{}],44:[function(require,module,exports){
/*global window: false */
"use strict";

var moment = require(1);
moment = typeof(moment) === 'function' ? moment : window.moment;

module.exports = function(Chart) {

  var helpers = Chart.helpers;
  var time = {
    units: [{
      name: 'millisecond',
      steps: [1, 2, 5, 10, 20, 50, 100, 250, 500]
    }, {
      name: 'second',
      steps: [1, 2, 5, 10, 30]
    }, {
      name: 'minute',
      steps: [1, 2, 5, 10, 30]
    }, {
      name: 'hour',
      steps: [1, 2, 3, 6, 12]
    }, {
      name: 'day',
      steps: [1, 2, 5]
    }, {
      name: 'week',
      maxStep: 4
    }, {
      name: 'month',
      maxStep: 3
    }, {
      name: 'quarter',
      maxStep: 4
    }, {
      name: 'year',
      maxStep: false
    }]
  };

  var defaultConfig = {
    position: "bottom",

    time: {
      parser: false, // false == a pattern string from http://momentjs.com/docs/#/parsing/string-format/ or a custom callback that converts its argument to a moment
      format: false, // DEPRECATED false == date objects, moment object, callback or a pattern string from http://momentjs.com/docs/#/parsing/string-format/
      unit: false, // false == automatic or override with week, month, year, etc.
      round: false, // none, or override with week, month, year, etc.
      displayFormat: false, // DEPRECATED
      isoWeekday: false, // override week start day - see http://momentjs.com/docs/#/get-set/iso-weekday/

      // defaults to unit's corresponding unitFormat below or override using pattern string from http://momentjs.com/docs/#/displaying/format/
      displayFormats: {
        'millisecond': 'h:mm:ss.SSS a', // 11:20:01.123 AM,
        'second': 'h:mm:ss a', // 11:20:01 AM
        'minute': 'h:mm:ss a', // 11:20:01 AM
        'hour': 'MMM D, hA', // Sept 4, 5PM
        'day': 'll', // Sep 4 2015
        'week': 'll', // Week 46, or maybe "[W]WW - YYYY" ?
        'month': 'MMM YYYY', // Sept 2015
        'quarter': '[Q]Q - YYYY', // Q3
        'year': 'YYYY' // 2015
      }
    },
    ticks: {
      autoSkip: false
    }
  };

  var TimeScale = Chart.Scale.extend({
    initialize: function() {
      if (!moment) {
        throw new Error('Chart.js - Moment.js could not be found! You must include it before Chart.js to use the time scale. Download at https://momentjs.com');
      }

      Chart.Scale.prototype.initialize.call(this);
    },
    getLabelMoment: function(datasetIndex, index) {
      return this.labelMoments[datasetIndex][index];
    },
    getMomentStartOf: function(tick) {
      var me = this;
      if (me.options.time.unit === 'week' && me.options.time.isoWeekday !== false) {
        return tick.clone().startOf('isoWeek').isoWeekday(me.options.time.isoWeekday);
      } else {
        return tick.clone().startOf(me.tickUnit);
      }
    },
    determineDataLimits: function() {
      var me = this;
      me.labelMoments = [];

      // Only parse these once. If the dataset does not have data as x,y pairs, we will use
      // these
      var scaleLabelMoments = [];
      if (me.chart.data.labels && me.chart.data.labels.length > 0) {
        helpers.each(me.chart.data.labels, function(label) {
          var labelMoment = me.parseTime(label);

          if (labelMoment.isValid()) {
            if (me.options.time.round) {
              labelMoment.startOf(me.options.time.round);
            }
            scaleLabelMoments.push(labelMoment);
          }
        }, me);

        me.firstTick = moment.min.call(me, scaleLabelMoments);
        me.lastTick = moment.max.call(me, scaleLabelMoments);
      } else {
        me.firstTick = null;
        me.lastTick = null;
      }

      helpers.each(me.chart.data.datasets, function(dataset, datasetIndex) {
        var momentsForDataset = [];
        var datasetVisible = me.chart.isDatasetVisible(datasetIndex);

        if (typeof dataset.data[0] === 'object' && dataset.data[0] !== null) {
          helpers.each(dataset.data, function(value) {
            var labelMoment = me.parseTime(me.getRightValue(value));

            if (labelMoment.isValid()) {
              if (me.options.time.round) {
                labelMoment.startOf(me.options.time.round);
              }
              momentsForDataset.push(labelMoment);

              if (datasetVisible) {
                // May have gone outside the scale ranges, make sure we keep the first and last ticks updated
                me.firstTick = me.firstTick !== null ? moment.min(me.firstTick, labelMoment) : labelMoment;
                me.lastTick = me.lastTick !== null ? moment.max(me.lastTick, labelMoment) : labelMoment;
              }
            }
          }, me);
        } else {
          // We have no labels. Use the ones from the scale
          momentsForDataset = scaleLabelMoments;
        }

        me.labelMoments.push(momentsForDataset);
      }, me);

      // Set these after we've done all the data
      if (me.options.time.min) {
        me.firstTick = me.parseTime(me.options.time.min);
      }

      if (me.options.time.max) {
        me.lastTick = me.parseTime(me.options.time.max);
      }

      // We will modify these, so clone for later
      me.firstTick = (me.firstTick || moment()).clone();
      me.lastTick = (me.lastTick || moment()).clone();
    },
    buildTicks: function() {
      var me = this;

      me.ctx.save();
      var tickFontSize = helpers.getValueOrDefault(me.options.ticks.fontSize, Chart.defaults.global.defaultFontSize);
      var tickFontStyle = helpers.getValueOrDefault(me.options.ticks.fontStyle, Chart.defaults.global.defaultFontStyle);
      var tickFontFamily = helpers.getValueOrDefault(me.options.ticks.fontFamily, Chart.defaults.global.defaultFontFamily);
      var tickLabelFont = helpers.fontString(tickFontSize, tickFontStyle, tickFontFamily);
      me.ctx.font = tickLabelFont;

      me.ticks = [];
      me.unitScale = 1; // How much we scale the unit by, ie 2 means 2x unit per step
      me.scaleSizeInUnits = 0; // How large the scale is in the base unit (seconds, minutes, etc)

      // Set unit override if applicable
      if (me.options.time.unit) {
        me.tickUnit = me.options.time.unit || 'day';
        me.displayFormat = me.options.time.displayFormats[me.tickUnit];
        me.scaleSizeInUnits = me.lastTick.diff(me.firstTick, me.tickUnit, true);
        me.unitScale = helpers.getValueOrDefault(me.options.time.unitStepSize, 1);
      } else {
        // Determine the smallest needed unit of the time
        var innerWidth = me.isHorizontal() ? me.width - (me.paddingLeft + me.paddingRight) : me.height - (me.paddingTop + me.paddingBottom);

        // Crude approximation of what the label length might be
        var tempFirstLabel = me.tickFormatFunction(me.firstTick, 0, []);
        var tickLabelWidth = me.ctx.measureText(tempFirstLabel).width;
        var cosRotation = Math.cos(helpers.toRadians(me.options.ticks.maxRotation));
        var sinRotation = Math.sin(helpers.toRadians(me.options.ticks.maxRotation));
        tickLabelWidth = (tickLabelWidth * cosRotation) + (tickFontSize * sinRotation);
        var labelCapacity = innerWidth / (tickLabelWidth);

        // Start as small as possible
        me.tickUnit = 'millisecond';
        me.scaleSizeInUnits = me.lastTick.diff(me.firstTick, me.tickUnit, true);
        me.displayFormat = me.options.time.displayFormats[me.tickUnit];

        var unitDefinitionIndex = 0;
        var unitDefinition = time.units[unitDefinitionIndex];

        // While we aren't ideal and we don't have units left
        while (unitDefinitionIndex < time.units.length) {
          // Can we scale this unit. If `false` we can scale infinitely
          me.unitScale = 1;

          if (helpers.isArray(unitDefinition.steps) && Math.ceil(me.scaleSizeInUnits / labelCapacity) < helpers.max(unitDefinition.steps)) {
            // Use one of the prefedined steps
            for (var idx = 0; idx < unitDefinition.steps.length; ++idx) {
              if (unitDefinition.steps[idx] >= Math.ceil(me.scaleSizeInUnits / labelCapacity)) {
                me.unitScale = helpers.getValueOrDefault(me.options.time.unitStepSize, unitDefinition.steps[idx]);
                break;
              }
            }

            break;
          } else if ((unitDefinition.maxStep === false) || (Math.ceil(me.scaleSizeInUnits / labelCapacity) < unitDefinition.maxStep)) {
            // We have a max step. Scale this unit
            me.unitScale = helpers.getValueOrDefault(me.options.time.unitStepSize, Math.ceil(me.scaleSizeInUnits / labelCapacity));
            break;
          } else {
            // Move to the next unit up
            ++unitDefinitionIndex;
            unitDefinition = time.units[unitDefinitionIndex];

            me.tickUnit = unitDefinition.name;
            var leadingUnitBuffer = me.firstTick.diff(me.getMomentStartOf(me.firstTick), me.tickUnit, true);
            var trailingUnitBuffer = me.getMomentStartOf(me.lastTick.clone().add(1, me.tickUnit)).diff(me.lastTick, me.tickUnit, true);
            me.scaleSizeInUnits = me.lastTick.diff(me.firstTick, me.tickUnit, true) + leadingUnitBuffer + trailingUnitBuffer;
            me.displayFormat = me.options.time.displayFormats[unitDefinition.name];
          }
        }
      }

      var roundedStart;

      // Only round the first tick if we have no hard minimum
      if (!me.options.time.min) {
        me.firstTick = me.getMomentStartOf(me.firstTick);
        roundedStart = me.firstTick;
      } else {
        roundedStart = me.getMomentStartOf(me.firstTick);
      }

      // Only round the last tick if we have no hard maximum
      if (!me.options.time.max) {
        var roundedEnd = me.getMomentStartOf(me.lastTick);
        if (roundedEnd.diff(me.lastTick, me.tickUnit, true) !== 0) {
          // Do not use end of because we need me to be in the next time unit
          me.lastTick = me.getMomentStartOf(me.lastTick.add(1, me.tickUnit));
        }
      }

      me.smallestLabelSeparation = me.width;

      helpers.each(me.chart.data.datasets, function(dataset, datasetIndex) {
        for (var i = 1; i < me.labelMoments[datasetIndex].length; i++) {
          me.smallestLabelSeparation = Math.min(me.smallestLabelSeparation, me.labelMoments[datasetIndex][i].diff(me.labelMoments[datasetIndex][i - 1], me.tickUnit, true));
        }
      }, me);

      // Tick displayFormat override
      if (me.options.time.displayFormat) {
        me.displayFormat = me.options.time.displayFormat;
      }

      // first tick. will have been rounded correctly if options.time.min is not specified
      me.ticks.push(me.firstTick.clone());

      // For every unit in between the first and last moment, create a moment and add it to the ticks tick
      for (var i = 1; i <= me.scaleSizeInUnits; ++i) {
        var newTick = roundedStart.clone().add(i, me.tickUnit);

        // Are we greater than the max time
        if (me.options.time.max && newTick.diff(me.lastTick, me.tickUnit, true) >= 0) {
          break;
        }

        if (i % me.unitScale === 0) {
          me.ticks.push(newTick);
        }
      }

      // Always show the right tick
      var diff = me.ticks[me.ticks.length - 1].diff(me.lastTick, me.tickUnit);
      if (diff !== 0 || me.scaleSizeInUnits === 0) {
        // this is a weird case. If the <max> option is the same as the end option, we can't just diff the times because the tick was created from the roundedStart
        // but the last tick was not rounded.
        if (me.options.time.max) {
          me.ticks.push(me.lastTick.clone());
          me.scaleSizeInUnits = me.lastTick.diff(me.ticks[0], me.tickUnit, true);
        } else {
          me.ticks.push(me.lastTick.clone());
          me.scaleSizeInUnits = me.lastTick.diff(me.firstTick, me.tickUnit, true);
        }
      }

      me.ctx.restore();
    },
    // Get tooltip label
    getLabelForIndex: function(index, datasetIndex) {
      var me = this;
      var label = me.chart.data.labels && index < me.chart.data.labels.length ? me.chart.data.labels[index] : '';

      if (typeof me.chart.data.datasets[datasetIndex].data[0] === 'object') {
        label = me.getRightValue(me.chart.data.datasets[datasetIndex].data[index]);
      }

      // Format nicely
      if (me.options.time.tooltipFormat) {
        label = me.parseTime(label).format(me.options.time.tooltipFormat);
      }

      return label;
    },
    // Function to format an individual tick mark
    tickFormatFunction: function(tick, index, ticks) {
      var formattedTick = tick.format(this.displayFormat);
      var tickOpts = this.options.ticks;
      var callback = helpers.getValueOrDefault(tickOpts.callback, tickOpts.userCallback);

      if (callback) {
        return callback(formattedTick, index, ticks);
      } else {
        return formattedTick;
      }
    },
    convertTicksToLabels: function() {
      var me = this;
      me.tickMoments = me.ticks;
      me.ticks = me.ticks.map(me.tickFormatFunction, me);
    },
    getPixelForValue: function(value, index, datasetIndex) {
      var me = this;
      if (!value || !value.isValid) {
        // not already a moment object
        value = moment(me.getRightValue(value));
      }
      var labelMoment = value && value.isValid && value.isValid() ? value : me.getLabelMoment(datasetIndex, index);

      if (labelMoment) {
        var offset = labelMoment.diff(me.firstTick, me.tickUnit, true);

        var decimal = offset !== 0 ? offset / me.scaleSizeInUnits : offset;

        if (me.isHorizontal()) {
          var innerWidth = me.width - (me.paddingLeft + me.paddingRight);
          var valueOffset = (innerWidth * decimal) + me.paddingLeft;

          return me.left + Math.round(valueOffset);
        } else {
          var innerHeight = me.height - (me.paddingTop + me.paddingBottom);
          var heightOffset = (innerHeight * decimal) + me.paddingTop;

          return me.top + Math.round(heightOffset);
        }
      }
    },
    getPixelForTick: function(index) {
      return this.getPixelForValue(this.tickMoments[index], null, null);
    },
    getValueForPixel: function(pixel) {
      var me = this;
      var innerDimension = me.isHorizontal() ? me.width - (me.paddingLeft + me.paddingRight) : me.height - (me.paddingTop + me.paddingBottom);
      var offset = (pixel - (me.isHorizontal() ? me.left + me.paddingLeft : me.top + me.paddingTop)) / innerDimension;
      offset *= me.scaleSizeInUnits;
      return me.firstTick.clone().add(moment.duration(offset, me.tickUnit).asSeconds(), 'seconds');
    },
    parseTime: function(label) {
      var me = this;
      if (typeof me.options.time.parser === 'string') {
        return moment(label, me.options.time.parser);
      }
      if (typeof me.options.time.parser === 'function') {
        return me.options.time.parser(label);
      }
      // Date objects
      if (typeof label.getMonth === 'function' || typeof label === 'number') {
        return moment(label);
      }
      // Moment support
      if (label.isValid && label.isValid()) {
        return label;
      }
      // Custom parsing (return an instance of moment)
      if (typeof me.options.time.format !== 'string' && me.options.time.format.call) {
        console.warn("options.time.format is deprecated and replaced by options.time.parser. See http://nnnick.github.io/Chart.js/docs-v2/#scales-time-scale");
        return me.options.time.format(label);
      }
      // Moment format parsing
      return moment(label, me.options.time.format);
    }
  });
  Chart.scaleService.registerScaleType("time", TimeScale, defaultConfig);

};

},{"1":1}]},{},[7])(7)
});
