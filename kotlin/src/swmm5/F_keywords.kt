// Generated from EPA SWMM 5.2.4 (keywords.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_keywords")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var BuildupTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L355, __L738, __L739, __L740, __L741, null)
@JvmField var CurveTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L742, __L743, __L744, __L745, __L746, __L747, __L662, __L748, __L749, __L750, __L751, __L752, null)
@JvmField var DividerTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L753, __L754, __L662, __L755, null)
@JvmField var EvapTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L756, __L757, __L692, __L758, __L1, __L759, __L760, null)
@JvmField var FileTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L761, __L762, __L763, __L764, __L765, __L766, null)
@JvmField var FileModeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L4, __L767, __L768, __L769, null)
@JvmField var FlowUnitWords: Array<CPtr?>? = arrayOf<CPtr?>(__L770, __L771, __L772, __L773, __L774, __L775, null)
@JvmField var ForceMainEqnWords: Array<CPtr?>? = arrayOf<CPtr?>(__L776, __L777, null)
@JvmField var GageDataWords: Array<CPtr?>? = arrayOf<CPtr?>(__L692, __L1, null)
@JvmField var InfilModelWords: Array<CPtr?>? = arrayOf<CPtr?>(__L778, __L779, __L780, __L781, __L782, null)
@JvmField var InertDampingWords: Array<CPtr?>? = arrayOf<CPtr?>(__L355, __L783, __L784, null)
@JvmField var LinkOffsetWords: Array<CPtr?>? = arrayOf<CPtr?>(__L664, __L785, null)
@JvmField var LinkTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L659, __L660, __L661, __L662, __L663, null)
@JvmField var LoadUnitsWords: Array<CPtr?>? = arrayOf<CPtr?>(__L786, __L787, __L788, null)
@JvmField var NodeTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L789, __L790, __L742, __L791, null)
@JvmField var NoneAllWords: Array<CPtr?>? = arrayOf<CPtr?>(__L355, __L351, null)
@JvmField var NormalFlowWords: Array<CPtr?>? = arrayOf<CPtr?>(__L673, __L792, __L793, __L355, null)
@JvmField var NormalizerWords: Array<CPtr?>? = arrayOf<CPtr?>(__L794, __L719, null)
@JvmField var NoYesWords: Array<CPtr?>? = arrayOf<CPtr?>(__L4, __L5, null)
@JvmField var OffOnWords: Array<CPtr?>? = arrayOf<CPtr?>(__L687, __L688, null)
@JvmField var OldRouteModelWords: Array<CPtr?>? = arrayOf<CPtr?>(__L355, __L795, __L796, __L797, __L798, null)
@JvmField var OptionWords: Array<CPtr?>? = arrayOf<CPtr?>(__L799, __L800, __L801, __L802, __L803, __L804, __L805, __L806, __L807, __L808, __L809, __L810, __L811, __L812, __L813, __L814, __L815, __L816, __L817, __L818, __L819, __L820, __L821, __L822, __L823, __L824, __L825, __L826, __L827, __L828, __L829, __L830, __L831, __L832, __L833, __L834, __L835, __L836, __L837, __L838, __L839, __L840, __L841, null)
@JvmField var OrificeTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L842, __L843, null)
@JvmField var OutfallTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L844, __L845, __L846, __L744, __L692, null)
@JvmField var PatternTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L757, __L847, __L848, __L849, null)
@JvmField var PondingUnitsWords: Array<CPtr?>? = arrayOf<CPtr?>(__L850, __L851, null)
@JvmField var ProcessVarWords: Array<CPtr?>? = arrayOf<CPtr?>(__L852, __L853, __L183, __L664, __L794, null)
@JvmField var PumpTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L854, __L855, __L856, __L857, __L858, __L859, null)
@JvmField var QualUnitsWords: Array<CPtr?>? = arrayOf<CPtr?>(__L860, __L861, __L862, null)
@JvmField var RainTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L694, __L666, __L863, null)
@JvmField var RainUnitsWords: Array<CPtr?>? = arrayOf<CPtr?>(__L864, __L865, null)
@JvmField var RelationWords: Array<CPtr?>? = arrayOf<CPtr?>(__L754, __L866, __L867, __L868, __L869, __L870, null)
@JvmField var ReportWords: Array<CPtr?>? = arrayOf<CPtr?>(__L871, __L872, __L873, __L657, __L658, __L874, __L875, __L746, __L876, __L877, null)
@JvmField var RouteModelWords: Array<CPtr?>? = arrayOf<CPtr?>(__L355, __L878, __L879, __L880, __L881, null)
@JvmField var RuleKeyWords: Array<CPtr?>? = arrayOf<CPtr?>(__L202, __L882, __L883, __L884, __L885, __L886, __L887, null)
@JvmField var SectWords: Array<CPtr?>? = arrayOf<CPtr?>(__L888, __L889, __L890, __L891, __L892, __L893, __L894, __L895, __L896, __L897, __L898, __L899, __L900, __L901, __L902, __L903, __L904, __L905, __L906, __L907, __L908, __L909, __L910, __L911, __L912, __L913, __L914, __L915, __L916, __L917, __L918, __L919, __L920, __L921, __L922, __L923, __L924, __L925, __L926, __L927, __L928, __L929, __L930, __L931, __L932, __L933, __L934, __L935, __L936, __L937, __L938, __L939, __L940, __L941, __L942, __L943, __L944, null)
@JvmField var SnowmeltWords: Array<CPtr?>? = arrayOf<CPtr?>(__L945, __L2, __L3, __L946, null)
@JvmField var SurchargeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L947, __L948, null)
@JvmField var TempKeyWords: Array<CPtr?>? = arrayOf<CPtr?>(__L692, __L1, __L949, __L950, __L951, null)
@JvmField var TransectKeyWords: Array<CPtr?>? = arrayOf<CPtr?>(__L952, __L203, __L953, null)
@JvmField var TreatTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L946, __L185, null)
@JvmField var UHTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L954, __L955, __L956, null)
@JvmField var VolUnitsWords: Array<CPtr?>? = arrayOf<CPtr?>(__L957, __L958, null)
@JvmField var VolUnitsWords2: Array<CPtr?>? = arrayOf<CPtr?>(__L959, __L960, null)
@JvmField var WashoffTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L355, __L739, __L961, __L962, null)
@JvmField var WeirTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L963, __L964, __L965, __L966, __L967, null)
@JvmField var XsectTypeWords: Array<CPtr?>? = arrayOf<CPtr?>(__L968, __L969, __L970, __L971, __L972, __L966, __L973, __L869, __L974, __L975, __L976, __L977, __L978, __L979, __L980, __L981, __L982, __L983, __L984, __L985, __L986, __L987, __L988, __L723, __L989, __L990, null)

