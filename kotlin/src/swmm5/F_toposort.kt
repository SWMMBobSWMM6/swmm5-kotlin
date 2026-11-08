// Generated from EPA SWMM 5.2.4 (toposort.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_toposort")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var InDegree: IntArray? = null
@JvmField var StartPos: IntArray? = null
@JvmField var AdjList: IntArray? = null
@JvmField var Stack: IntArray? = null
@JvmField var First: Int = 0
@JvmField var Last: Int = 0
@JvmField var Examined: CPtr? = null
@JvmField var InTree: CPtr? = null
@JvmField var LoopLinks: IntArray? = null
@JvmField var LoopLinksLast: Int = 0

// toposort.c:57
fun toposort_sortLinks(sortedLinks: IntArray?) {
    var i: Int = 0
    var n: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        sortedLinks!![i] = i
        i++
    }
    if ((RouteModel == DW)) {
        checkDummyLinks()
        if ((ErrorCode != 0)) {
            return
        }
        i = 0
        f2@ while ((i < Nobjects!![NODE])) {
            Node!![i]!!.degree = 0
            i++
        }
        i = 0
        f3@ while ((i < Nobjects!![LINK])) {
            n = Link!![i]!!.node1
            if ((Link!![i]!!.direction < 0)) {
                n = Link!![i]!!.node2
            }
            if ((Node!![n]!!.type == OUTFALL)) {
                if ((Link!![i]!!.direction < 0)) {
                    n = Link!![i]!!.node1
                } else {
                    n = Link!![i]!!.node2
                }
                Node!![n]!!.degree++
            } else {
                Node!![n]!!.degree++
            }
            i++
        }
        return
    }
    if ((ErrorCode != 0)) {
        return
    }
    InDegree = IntArray(((Nobjects!![NODE]).toLong()).toInt())
    StartPos = IntArray(((Nobjects!![NODE]).toLong()).toInt())
    AdjList = IntArray(((Nobjects!![LINK]).toLong()).toInt())
    Stack = IntArray(((Nobjects!![NODE]).toLong()).toInt())
    if (((((InDegree == null) || (StartPos == null)) || (AdjList == null)) || (Stack == null))) {
        report_writeErrorMsg(ERR_MEMORY, __L0)
    } else {
        createAdjList(DIRECTED)
        adjustAdjList()
        i = 0
        f4@ while ((i < Nobjects!![NODE])) {
            InDegree!![i] = 0
            i++
        }
        i = 0
        f5@ while ((i < Nobjects!![LINK])) {
            InDegree!![Link!![i]!!.node2]++
            i++
        }
        n = topoSort(sortedLinks)
    }
    if ((InDegree != null)) {
        R.free(InDegree)
        InDegree = (null)
    }
    if ((StartPos != null)) {
        R.free(StartPos)
        StartPos = (null)
    }
    if ((AdjList != null)) {
        R.free(AdjList)
        AdjList = (null)
    }
    if ((Stack != null)) {
        R.free(Stack)
        Stack = (null)
    }
    if (((!(ErrorCode != 0)) && (n != Nobjects!![LINK]))) {
        report_writeErrorMsg(ERR_LOOP, __L0)
        findCycles()
    }
}

// toposort.c:142
fun createAdjList(listType: Int) {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        Node!![i]!!.degree = 0
        i++
    }
    j = 0
    f2@ while ((j < Nobjects!![LINK])) {
        Node!![Link!![j]!!.node1]!!.degree++
        if ((listType == UNDIRECTED)) {
            Node!![Link!![j]!!.node2]!!.degree++
        }
        j++
    }
    StartPos!![0] = 0
    i = 0
    f3@ while ((i < (Nobjects!![NODE] - 1))) {
        StartPos!![(i + 1)] = (StartPos!![i] + Node!![i]!!.degree)
        Node!![i]!!.degree = 0
        i++
    }
    (Node!![(Nobjects!![NODE] - 1)])!!.degree = 0
    j = 0
    f4@ while ((j < Nobjects!![LINK])) {
        i = Link!![j]!!.node1
        k = (StartPos!![i] + Node!![i]!!.degree)
        AdjList!![k] = j
        Node!![i]!!.degree++
        if ((listType == UNDIRECTED)) {
            i = Link!![j]!!.node2
            k = (StartPos!![i] + Node!![i]!!.degree)
            AdjList!![k] = j
            Node!![i]!!.degree++
        }
        j++
    }
}

// toposort.c:193
fun adjustAdjList() {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        if ((Node!![i]!!.type != DIVIDER)) {
            i++
            continue@f1
        }
        if ((Node!![i]!!.degree != 2)) {
            i++
            continue@f1
        }
        k = Node!![i]!!.subIndex
        m = StartPos!![i]
        j = AdjList!![m]
        if ((j == Divider!![k]!!.link)) {
            AdjList!![m] = AdjList!![(m + 1)]
            AdjList!![(m + 1)] = j
        }
        i++
    }
}

// toposort.c:225
fun topoSort(sortedLinks: IntArray?): Int {
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    var n: Int = 0
    var i1: Int = 0
    var i2: Int = 0
    var k1: Int = 0
    var k2: Int = 0
    First = 0
    Last = (-1)
    i = 0
    f1@ while ((i < Nobjects!![NODE])) {
        if ((InDegree!![i] == 0)) {
            Last++
            Stack!![Last] = i
        }
        i++
    }
    n = 0
    w2@ while ((First <= Last)) {
        i1 = Stack!![First]
        k1 = StartPos!![i1]
        k2 = (k1 + Node!![i1]!!.degree)
        k = k1
        f3@ while ((k < k2)) {
            j = AdjList!![k]
            sortedLinks!![n] = j
            n++
            i2 = Link!![j]!!.node2
            InDegree!![i2]--
            if ((InDegree!![i2] == 0)) {
                Last++
                Stack!![Last] = i2
            }
            k++
        }
        First++
    }
    return n
}

// toposort.c:284
fun findCycles() {
    var i: Int = 0
    AdjList = IntArray(((2L * (Nobjects!![LINK]).toLong())).toInt())
    StartPos = IntArray(((Nobjects!![NODE]).toLong()).toInt())
    Stack = IntArray(((Nobjects!![NODE]).toLong()).toInt())
    Examined = R.buf(((Nobjects!![NODE]).toLong()).toInt())
    InTree = R.buf(((Nobjects!![LINK]).toLong()).toInt())
    LoopLinks = IntArray(((Nobjects!![LINK]).toLong()).toInt())
    if (((((((StartPos != null) && (AdjList != null)) && (Stack != null)) && (Examined != null)) && (InTree != null)) && (LoopLinks != null))) {
        createAdjList(UNDIRECTED)
        i = 0
        f1@ while ((i < Nobjects!![NODE])) {
            Examined!!.s(i, 0)
            i++
        }
        i = 0
        f2@ while ((i < Nobjects!![LINK])) {
            InTree!!.s(i, 0)
            i++
        }
        i = 0
        f3@ while ((i < Nobjects!![NODE])) {
            if ((Examined!!.g(i) != 0)) {
                i++
                continue@f3
            }
            Last = (-1)
            findSpanningTree(i)
            i++
        }
    }
    if ((StartPos != null)) {
        R.free(StartPos)
        StartPos = (null)
    }
    if ((AdjList != null)) {
        R.free(AdjList)
        AdjList = (null)
    }
    if ((Stack != null)) {
        R.free(Stack)
        Stack = (null)
    }
    if ((Examined != null)) {
        R.free(Examined)
        Examined = null
    }
    if ((InTree != null)) {
        R.free(InTree)
        InTree = null
    }
    if ((LoopLinks != null)) {
        R.free(LoopLinks)
        LoopLinks = (null)
    }
}

// toposort.c:330
fun findSpanningTree(startNode: Int) {
    var nextNode: Int = 0
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    m = StartPos!![startNode]
    f1@ while ((m < (StartPos!![startNode] + Node!![startNode]!!.degree))) {
        k = AdjList!![m]
        if ((Link!![k]!!.node1 == startNode)) {
            j = Link!![k]!!.node2
        } else {
            j = Link!![k]!!.node1
        }
        if ((InTree!!.g(k) == 0)) {
            if ((Examined!!.g(j) != 0)) {
                InTree!!.s(k, 2)
                evalLoop(k)
            } else {
                Examined!!.s(j, 1)
                Last++
                Stack!![Last] = j
                InTree!!.s(k, 1)
            }
        }
        m++
    }
    if ((Last >= 0)) {
        nextNode = Stack!![Last]
        Last--
        findSpanningTree(nextNode)
    }
}

// toposort.c:385
fun evalLoop(startLink: Int) {
    var i: Int = 0
    var i1: Int = 0
    var i2: Int = 0
    var j: Int = 0
    var kount: Int = 0
    var isCycle: Int = 0
    LoopLinksLast = 0
    LoopLinks!![0] = startLink
    i1 = Link!![startLink]!!.node1
    i2 = Link!![startLink]!!.node2
    if (!(traceLoop(i1, i2, startLink) != 0)) {
        return
    }
    isCycle = 1
    j = LoopLinks!![0]
    i2 = Link!![j]!!.node2
    i = 1
    f1@ while ((i <= LoopLinksLast)) {
        j = LoopLinks!![i]
        i1 = Link!![j]!!.node1
        if ((i1 == i2)) {
            i2 = Link!![j]!!.node2
        } else {
            isCycle = 0
            break@f1
        }
        i++
    }
    if ((isCycle != 0)) {
        kount = 0
        i = 0
        f2@ while ((i <= LoopLinksLast)) {
            if (((kount % 5) == 0)) {
                R.fprintf(Frpt!!.file, __L276)
            }
            kount++
            R.fprintf(Frpt!!.file, __L651, Link!![LoopLinks!![i]]!!.ID)
            if ((i < LoopLinksLast)) {
                R.fprintf(Frpt!!.file, __L652)
            }
            i++
        }
    }
}

// toposort.c:440
fun traceLoop(i1: Int, i2: Int, k1: Int): Int {
    var j: Int = 0
    var k: Int = 0
    var m: Int = 0
    if ((i1 == i2)) {
        return 1
    }
    m = StartPos!![i1]
    f1@ while ((m < (StartPos!![i1] + Node!![i1]!!.degree))) {
        k = AdjList!![m]
        if (((k == k1) || (InTree!!.g(k) != 1))) {
            m++
            continue@f1
        }
        if ((Link!![k]!!.node1 == i1)) {
            j = Link!![k]!!.node2
        } else {
            j = Link!![k]!!.node1
        }
        if ((traceLoop(j, i2, k) != 0)) {
            LoopLinksLast++
            LoopLinks!![LoopLinksLast] = k
            return 1
        }
        m++
    }
    return 0
}

// toposort.c:482
fun checkDummyLinks() {
    var i: Int = 0
    var j: Int = 0
    var marked: IntArray? = null
    marked = IntArray(((Nobjects!![NODE]).toLong()).toInt())
    if ((marked == null)) {
        report_writeErrorMsg(ERR_MEMORY, __L0)
        return
    }
    i = 0
    f1@ while ((i < Nobjects!![LINK])) {
        j = Link!![i]!!.node2
        if ((Link!![i]!!.direction < 0)) {
            j = Link!![i]!!.node1
        }
        if (((((Link!![i]!!.type == CONDUIT) && (Link!![i]!!.xsect!!.type == DUMMY))) || (((Link!![i]!!.type == PUMP) && (Pump!![Link!![i]!!.subIndex]!!.type == IDEAL_PUMP))))) {
            if ((marked!![j] == 0)) {
                marked!![j] = 1
            }
        } else {
            marked!![j] = (-1)
        }
        i++
    }
    i = 0
    f2@ while ((i < Nobjects!![LINK])) {
        if (((((Link!![i]!!.type == CONDUIT) && (Link!![i]!!.xsect!!.type == DUMMY))) || (((Link!![i]!!.type == PUMP) && (Pump!![Link!![i]!!.subIndex]!!.type == IDEAL_PUMP))))) {
            j = Link!![i]!!.node1
            if ((marked!![j] > 0)) {
                report_writeErrorMsg(ERR_DUMMY_LINK, Node!![j]!!.ID)
            }
        }
        i++
    }
    if ((marked != null)) {
        R.free(marked)
        marked = (null)
    }
}

