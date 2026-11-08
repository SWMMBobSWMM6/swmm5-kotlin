// Generated from EPA SWMM 5.2.4 (xsect.c) by tools/c2x/emit_kotlin.py. Do not edit by hand;
// fix the translator, R.kt or Overrides.kt instead.
@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "UNUSED_PARAMETER", "NAME_SHADOWING", "UNREACHABLE_CODE",
               "VARIABLE_WITH_REDUNDANT_INITIALIZER", "UNNECESSARY_NOT_NULL_ASSERTION", "UNUSED_EXPRESSION",
               "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST", "LocalVariableName",
               "FunctionName", "PropertyName", "ObjectPropertyName", "ClassName", "KotlinConstantConditions",
               "ReplaceWithOperatorAssignment", "CascadeIf", "LiftReturnOrAssignment", "RedundantExplicitType")
@file:JvmName("F_xsect")
package swmm5

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

@JvmField var N_A_Circ: Int = 51
@JvmField var A_Circ: DoubleArray? = doubleArrayOf(0.0, 0.00471, 0.0134, 0.024446, 0.0374, 0.05208, 0.0680, 0.08505, 0.1033, 0.12236, 0.1423, 0.16310, 0.1845, 0.20665, 0.2292, 0.25236, 0.2759, 0.29985, 0.3242, 0.34874, 0.3736, 0.39878, 0.4237, 0.44907, 0.4745, 0.500, 0.5255, 0.55093, 0.5763, 0.60135, 0.6264, 0.65126, 0.6758, 0.70015, 0.7241, 0.74764, 0.7708, 0.79335, 0.8154, 0.83690, 0.8576, 0.87764, 0.8967, 0.91495, 0.9320, 0.94792, 0.9626, 0.97555, 0.9866, 0.99516, 1.000)
@JvmField var N_R_Circ: Int = 51
@JvmField var R_Circ: DoubleArray? = doubleArrayOf(0.0100, 0.0528, 0.1048, 0.1556, 0.2052, 0.254, 0.3016, 0.3484, 0.3944, 0.4388, 0.4824, 0.5248, 0.5664, 0.6064, 0.6456, 0.6836, 0.7204, 0.7564, 0.7912, 0.8244, 0.8568, 0.888, 0.9176, 0.9464, 0.9736, 1.00, 1.024, 1.048, 1.070, 1.0912, 1.110, 1.1272, 1.144, 1.1596, 1.174, 1.1848, 1.194, 1.2024, 1.210, 1.2148, 1.217, 1.2172, 1.215, 1.2104, 1.203, 1.192, 1.178, 1.1584, 1.132, 1.094, 1.000)
@JvmField var N_Y_Circ: Int = 51
@JvmField var Y_Circ: DoubleArray? = doubleArrayOf(0.0, 0.05236, 0.08369, 0.11025, 0.13423, 0.15643, 0.17755, 0.19772, 0.21704, 0.23581, 0.25412, 0.27194, 0.28948, 0.30653, 0.32349, 0.34017, 0.35666, 0.37298, 0.38915, 0.40521, 0.42117, 0.43704, 0.45284, 0.46858, 0.4843, 0.50000, 0.51572, 0.53146, 0.54723, 0.56305, 0.57892, 0.59487, 0.61093, 0.62710, 0.64342, 0.65991, 0.67659, 0.69350, 0.71068, 0.72816, 0.74602, 0.76424, 0.78297, 0.80235, 0.82240, 0.84353, 0.86563, 0.88970, 0.91444, 0.94749, 1.0)
@JvmField var N_S_Circ: Int = 51
@JvmField var S_Circ: DoubleArray? = doubleArrayOf(0.0, 0.00529, 0.01432, 0.02559, 0.03859, 0.05304, 0.06877, 0.08551, 0.10326, 0.12195, 0.14144, 0.16162, 0.18251, 0.2041, 0.22636, 0.24918, 0.27246, 0.29614, 0.32027, 0.34485, 0.36989, 0.39531, 0.42105, 0.44704, 0.47329, 0.4998, 0.52658, 0.55354, 0.58064, 0.60777, 0.63499, 0.66232, 0.68995, 0.7177, 0.74538, 0.77275, 0.79979, 0.82658, 0.8532, 0.87954, 0.90546, 0.93095, 0.95577, 0.97976, 1.00291, 1.02443, 1.04465, 1.06135, 1.08208, 1.07662, 1.0)
@JvmField var N_W_Circ: Int = 51
@JvmField var W_Circ: DoubleArray? = doubleArrayOf(0.0, 0.2800, 0.3919, 0.4750, 0.5426, 0.6000, 0.6499, 0.6940, 0.7332, 0.7684, 0.8000, 0.8285, 0.8542, 0.8773, 0.8980, 0.9165, 0.9330, 0.9474, 0.9600, 0.9708, 0.9798, 0.9871, 0.9928, 0.9968, 0.9992, 1.000, 0.9992, 0.9968, 0.9928, 0.9871, 0.9798, 0.9708, 0.9600, 0.9474, 0.9330, 0.9165, 0.8980, 0.8773, 0.8542, 0.8285, 0.8000, 0.7684, 0.7332, 0.6940, 0.6499, 0.6000, 0.5426, 0.4750, 0.3919, 0.2800, 0.0)
@JvmField var N_A_Egg: Int = 26
@JvmField var A_Egg: DoubleArray? = doubleArrayOf(0.0000, 0.0116, 0.0316, 0.0550, 0.0850, 0.1200, 0.1555, 0.1900, 0.2250, 0.2750, 0.3200, 0.3700, 0.4200, 0.4700, 0.5150, 0.5700, 0.6200, 0.6800, 0.7300, 0.7800, 0.8350, 0.8850, 0.9250, 0.9550, 0.9800, 1.000)
@JvmField var N_R_Egg: Int = 26
@JvmField var R_Egg: DoubleArray? = doubleArrayOf(0.0100, 0.1300, 0.2440, 0.3020, 0.3860, 0.4650, 0.5360, 0.6110, 0.6760, 0.7350, 0.7910, 0.8540, 0.9040, 0.9410, 1.008, 1.045, 1.076, 1.115, 1.146, 1.162, 1.186, 1.193, 1.186, 1.162, 1.107, 1.000)
@JvmField var N_Y_Egg: Int = 51
@JvmField var Y_Egg: DoubleArray? = doubleArrayOf(0.0, 0.04912, 0.08101, 0.11128, 0.14161, 0.16622, 0.18811, 0.21356, 0.23742, 0.25742, 0.27742, 0.29741, 0.31742, 0.33742, 0.35747, 0.37364, 0.4, 0.41697, 0.43372, 0.45, 0.46374, 0.47747, 0.49209, 0.50989, 0.53015, 0.55, 0.56429, 0.57675, 0.58834, 0.6, 0.61441, 0.62967, 0.64582, 0.66368, 0.68209, 0.7, 0.71463, 0.72807, 0.74074, 0.75296, 0.765, 0.77784, 0.79212, 0.80945, 0.82936, 0.85, 0.86731, 0.88769, 0.914, 0.95, 1.0)
@JvmField var N_S_Egg: Int = 51
@JvmField var S_Egg: DoubleArray? = doubleArrayOf(0.0, 0.00295, 0.01331, 0.02629, 0.04, 0.05657, 0.075, 0.09432, 0.11473, 0.13657, 0.15894, 0.1803, 0.20036, 0.22, 0.23919, 0.25896, 0.28, 0.30504, 0.33082, 0.35551, 0.37692, 0.39809, 0.42, 0.44625, 0.47321, 0.5, 0.52255, 0.54481, 0.56785, 0.59466, 0.62485, 0.65518, 0.68181, 0.70415, 0.72585, 0.74819, 0.77482, 0.80515, 0.83534, 0.86193, 0.88465, 0.9069, 0.93, 0.95866, 0.98673, 1.01238, 1.03396, 1.05, 1.06517, 1.0538, 1.0)
@JvmField var N_W_Egg: Int = 26
@JvmField var W_Egg: DoubleArray? = doubleArrayOf(0.0, 0.3250, 0.4270, 0.5080, 0.5820, 0.6420, 0.6960, 0.7460, 0.7910, 0.8360, 0.8660, 0.8960, 0.9260, 0.9560, 0.9700, 0.9850, 1.000, 0.9850, 0.9700, 0.9400, 0.8960, 0.8360, 0.7640, 0.6420, 0.3100, 0.0)
@JvmField var N_A_Horseshoe: Int = 26
@JvmField var A_Horseshoe: DoubleArray? = doubleArrayOf(0.0000, 0.0181, 0.0508, 0.0908, 0.1326, 0.1757, 0.2201, 0.2655, 0.3118, 0.3587, 0.4064, 0.4542, 0.5023, 0.5506, 0.5987, 0.6462, 0.6931, 0.7387, 0.7829, 0.8253, 0.8652, 0.9022, 0.9356, 0.9645, 0.9873, 1.000)
@JvmField var N_R_Horseshoe: Int = 26
@JvmField var R_Horseshoe: DoubleArray? = doubleArrayOf(0.0100, 0.1040, 0.2065, 0.3243, 0.4322, 0.5284, 0.6147, 0.6927, 0.7636, 0.8268, 0.8873, 0.9417, 0.9905, 1.036, 1.077, 1.113, 1.143, 1.169, 1.189, 1.202, 1.208, 1.206, 1.195, 1.170, 1.126, 1.000)
@JvmField var N_Y_Horseshoe: Int = 51
@JvmField var Y_Horseshoe: DoubleArray? = doubleArrayOf(0.0, 0.04146, 0.07033, 0.09098, 0.10962, 0.12921, 0.14813, 0.16701, 0.18565, 0.20401, 0.22211, 0.23998, 0.25769, 0.27524, 0.29265, 0.3099, 0.32704, 0.34406, 0.36101, 0.3779, 0.39471, 0.41147, 0.42818, 0.44484, 0.46147, 0.47807, 0.49468, 0.51134, 0.52803, 0.54474, 0.56138, 0.57804, 0.59478, 0.61171, 0.62881, 0.64609, 0.6635, 0.68111, 0.69901, 0.71722, 0.73583, 0.7549, 0.77447, 0.79471, 0.81564, 0.83759, 0.86067, 0.88557, 0.91159, 0.9452, 1.0)
@JvmField var N_S_Horseshoe: Int = 51
@JvmField var S_Horseshoe: DoubleArray? = doubleArrayOf(0.0, 0.00467, 0.01237, 0.02268, 0.03515, 0.04943, 0.06525, 0.08212, 0.10005, 0.11891, 0.13856, 0.15896, 0.18004, 0.20172, 0.22397, 0.24677, 0.27006, 0.2938, 0.3179, 0.34237, 0.3672, 0.39239, 0.41792, 0.44374, 0.46984, 0.49619, 0.52276, 0.5495, 0.5764, 0.60345, 0.63065, 0.65795, 0.68531, 0.71271, 0.74009, 0.76738, 0.79451, 0.82144, 0.84814, 0.8745, 0.90057, 0.92652, 0.95244, 0.97724, 0.99988, 1.02048, 1.03989, 1.05698, 1.07694, 1.07562, 1.0)
@JvmField var N_W_Horseshoe: Int = 26
@JvmField var W_Horseshoe: DoubleArray? = doubleArrayOf(0.0, 0.5878, 0.8772, 0.8900, 0.9028, 0.9156, 0.9284, 0.9412, 0.9540, 0.9668, 0.9798, 0.9928, 0.9992, 0.9992, 0.9928, 0.9798, 0.9600, 0.9330, 0.8980, 0.8542, 0.8000, 0.7332, 0.6499, 0.5426, 0.3919, 0.0)
@JvmField var N_Y_Gothic: Int = 51
@JvmField var Y_Gothic: DoubleArray? = doubleArrayOf(0.0, 0.04522, 0.07825, 0.10646, 0.12645, 0.14645, 0.16787, 0.18641, 0.20129, 0.22425, 0.24129, 0.25624, 0.27344, 0.29097, 0.30529, 0.32607, 0.33755, 0.35073, 0.36447, 0.37558, 0.4, 0.4181, 0.43648, 0.45374, 0.46805, 0.48195, 0.49626, 0.51352, 0.5319, 0.55, 0.56416, 0.57787, 0.59224, 0.6095, 0.62941, 0.65, 0.67064, 0.69055, 0.70721, 0.72031, 0.73286, 0.74632, 0.76432, 0.78448, 0.80421, 0.82199, 0.84363, 0.87423, 0.90617, 0.93827, 1.0)
@JvmField var N_S_Gothic: Int = 51
@JvmField var S_Gothic: DoubleArray? = doubleArrayOf(0.0, 0.005, 0.0174, 0.03098, 0.04272, 0.055, 0.0698, 0.0862, 0.10461, 0.12463, 0.145, 0.16309, 0.18118, 0.2, 0.22181, 0.24487, 0.26888, 0.2938, 0.31901, 0.34389, 0.36564, 0.38612, 0.4072, 0.43, 0.45868, 0.48895, 0.52, 0.55032, 0.5804, 0.61, 0.63762, 0.66505, 0.6929, 0.72342, 0.75467, 0.785, 0.81165, 0.83654, 0.86, 0.88253, 0.90414, 0.925, 0.94486, 0.96475, 0.98567, 1.00833, 1.03, 1.0536, 1.065, 1.055, 1.0)
@JvmField var N_W_Gothic: Int = 21
@JvmField var W_Gothic: DoubleArray? = doubleArrayOf(0.0, 0.286, 0.643, 0.762, 0.833, 0.905, 0.952, 0.976, 0.976, 1.0, 1.0, 0.976, 0.976, 0.952, 0.905, 0.833, 0.762, 0.667, 0.524, 0.357, 0.0)
@JvmField var N_Y_Catenary: Int = 51
@JvmField var Y_Catenary: DoubleArray? = doubleArrayOf(0.0, 0.02974, 0.06439, 0.08433, 0.10549, 0.12064, 0.13952, 0.1556, 0.17032, 0.18512, 0.20057, 0.21995, 0.24011, 0.25892, 0.27595, 0.29214, 0.30802, 0.32372, 0.33894, 0.35315, 0.36557, 0.37833, 0.3923, 0.4097, 0.42982, 0.45, 0.46769, 0.48431, 0.5, 0.51466, 0.52886, 0.54292, 0.55729, 0.57223, 0.5878, 0.60428, 0.62197, 0.64047, 0.6598, 0.67976, 0.7, 0.71731, 0.73769, 0.76651, 0.8, 0.8209, 0.84311, 0.87978, 0.91576, 0.95, 1.0)
@JvmField var N_S_Catenary: Int = 51
@JvmField var S_Catenary: DoubleArray? = doubleArrayOf(0.0, 0.00605, 0.01455, 0.0254, 0.03863, 0.0543, 0.07127, 0.08778, 0.10372, 0.12081, 0.14082, 0.16375, 0.18779, 0.21157, 0.23478, 0.25818, 0.28244, 0.30741, 0.33204, 0.35505, 0.37465, 0.39404, 0.41426, 0.43804, 0.46531, 0.49357, 0.52187, 0.54925, 0.57647, 0.60321, 0.62964, 0.65639, 0.68472, 0.71425, 0.74303, 0.76827, 0.79168, 0.815, 0.84094, 0.86707, 0.89213, 0.91607, 0.94, 0.96604, 0.99, 1.00714, 1.02158, 1.03814, 1.05, 1.05, 1.0)
@JvmField var N_W_Catenary: Int = 21
@JvmField var W_Catenary: DoubleArray? = doubleArrayOf(0.0, 0.6667, 0.8222, 0.9111, 0.9778, 1.0000, 1.0000, 0.9889, 0.9778, 0.9556, 0.9333, 0.8889, 0.8444, 0.8000, 0.7556, 0.7000, 0.6333, 0.5556, 0.4444, 0.3333, 0.0)
@JvmField var N_Y_SemiEllip: Int = 51
@JvmField var Y_SemiEllip: DoubleArray? = doubleArrayOf(0.0, 0.03075, 0.05137, 0.07032, 0.09, 0.11323, 0.13037, 0.14519, 0.15968, 0.18459, 0.19531, 0.21354, 0.22694, 0.23947, 0.25296, 0.265, 0.27784, 0.29212, 0.3097, 0.32982, 0.35, 0.36738, 0.3839, 0.4, 0.41667, 0.43333, 0.45, 0.46697, 0.48372, 0.5, 0.51374, 0.52747, 0.54209, 0.5595, 0.57941, 0.6, 0.62, 0.64, 0.66, 0.68, 0.7, 0.71843, 0.73865, 0.76365, 0.7926, 0.82088, 0.85, 0.88341, 0.90998, 0.93871, 1.0)
@JvmField var N_S_SemiEllip: Int = 51
@JvmField var S_SemiEllip: DoubleArray? = doubleArrayOf(0.0, 0.00438, 0.01227, 0.02312, 0.03638, 0.05145, 0.06783, 0.085, 0.10093, 0.11752, 0.1353, 0.15626, 0.17917, 0.20296, 0.22654, 0.24962, 0.27269, 0.29568, 0.31848, 0.34152, 0.365, 0.38941, 0.41442, 0.44, 0.46636, 0.49309, 0.52, 0.54628, 0.57285, 0.6, 0.62949, 0.65877, 0.68624, 0.71017, 0.73304, 0.75578, 0.77925, 0.80368, 0.83114, 0.8595, 0.88592, 0.90848, 0.93, 0.95292, 0.97481, 0.99374, 1.01084, 1.02858, 1.04543, 1.05, 1.0)
@JvmField var N_W_SemiEllip: Int = 21
@JvmField var W_SemiEllip: DoubleArray? = doubleArrayOf(0.0, 0.7000, 0.9800, 1.0000, 1.0000, 1.0000, 0.9900, 0.9800, 0.9600, 0.9400, 0.9100, 0.8800, 0.8400, 0.8000, 0.7500, 0.7000, 0.6400, 0.5600, 0.4600, 0.3400, 0.0)
@JvmField var N_A_Baskethandle: Int = 26
@JvmField var A_Baskethandle: DoubleArray? = doubleArrayOf(0.0000, 0.0173, 0.0457, 0.0828, 0.1271, 0.1765, 0.2270, 0.2775, 0.3280, 0.3780, 0.4270, 0.4765, 0.5260, 0.5740, 0.6220, 0.6690, 0.7160, 0.7610, 0.8030, 0.8390, 0.8770, 0.9110, 0.9410, 0.9680, 0.9880, 1.000)
@JvmField var N_R_Baskethandle: Int = 26
@JvmField var R_Baskethandle: DoubleArray? = doubleArrayOf(0.0100, 0.0952, 0.1890, 0.2730, 0.3690, 0.4630, 0.5600, 0.6530, 0.7430, 0.8220, 0.8830, 0.9490, 0.9990, 1.055, 1.095, 1.141, 1.161, 1.188, 1.206, 1.206, 1.206, 1.205, 1.196, 1.168, 1.127, 1.000)
@JvmField var N_Y_BasketHandle: Int = 51
@JvmField var Y_BasketHandle: DoubleArray? = doubleArrayOf(0.0, 0.04112, 0.0738, 0.1, 0.12236, 0.14141, 0.15857, 0.17462, 0.18946, 0.20315, 0.21557, 0.22833, 0.2423, 0.25945, 0.27936, 0.3, 0.3204, 0.34034, 0.35892, 0.37595, 0.39214, 0.40802, 0.42372, 0.43894, 0.45315, 0.46557, 0.47833, 0.4923, 0.50945, 0.52936, 0.55, 0.57, 0.59, 0.61023, 0.63045, 0.65, 0.66756, 0.68413, 0.7, 0.71481, 0.72984, 0.74579, 0.76417, 0.78422, 0.80477, 0.82532, 0.85, 0.88277, 0.915, 0.95, 1.0)
@JvmField var N_S_BasketHandle: Int = 51
@JvmField var S_BasketHandle: DoubleArray? = doubleArrayOf(0.0, 0.00758, 0.01812, 0.03, 0.03966, 0.04957, 0.0623, 0.07849, 0.09618, 0.11416, 0.13094, 0.14808, 0.16583, 0.18381, 0.20294, 0.225, 0.2547, 0.28532, 0.31006, 0.32804, 0.34555, 0.36944, 0.40032, 0.43203, 0.46004, 0.47849, 0.49591, 0.51454, 0.5381, 0.56711, 0.6, 0.64092, 0.68136, 0.71259, 0.73438, 0.755, 0.78625, 0.8188, 0.85, 0.8679, 0.88483, 0.90431, 0.9369, 0.97388, 1.00747, 1.033, 1.05, 1.05464, 1.06078, 1.055, 1.0)
@JvmField var N_W_BasketHandle: Int = 26
@JvmField var W_BasketHandle: DoubleArray? = doubleArrayOf(0.0, 0.49, 0.667, 0.82, 0.93, 1.00, 1.00, 1.00, 0.997, 0.994, 0.988, 0.982, 0.967, 0.948, 0.928, 0.904, 0.874, 0.842, 0.798, 0.75, 0.697, 0.637, 0.567, 0.467, 0.342, 0.0)
@JvmField var N_Y_SemiCirc: Int = 51
@JvmField var Y_SemiCirc: DoubleArray? = doubleArrayOf(0.0, 0.04102, 0.07407, 0.1, 0.11769, 0.13037, 0.14036, 0.15, 0.16546, 0.18213, 0.2, 0.22018, 0.2403, 0.25788, 0.27216, 0.285, 0.29704, 0.30892, 0.32128, 0.33476, 0.35, 0.36927, 0.38963, 0.41023, 0.43045, 0.45, 0.46769, 0.48431, 0.5, 0.51443, 0.52851, 0.54271, 0.55774, 0.57388, 0.59101, 0.60989, 0.63005, 0.65, 0.66682, 0.68318, 0.7, 0.71675, 0.73744, 0.76651, 0.8, 0.8209, 0.84311, 0.87978, 0.91576, 0.95, 1.0)
@JvmField var N_S_SemiCirc: Int = 51
@JvmField var S_SemiCirc: DoubleArray? = doubleArrayOf(0.0, 0.00757, 0.01815, 0.03, 0.0358, 0.04037, 0.04601, 0.055, 0.07475, 0.09834, 0.125, 0.1557, 0.18588, 0.20883, 0.223, 0.23472, 0.24667, 0.26758, 0.29346, 0.32124, 0.35, 0.3772, 0.4054, 0.43541, 0.46722, 0.5, 0.53532, 0.56935, 0.6, 0.61544, 0.62811, 0.6417, 0.66598, 0.7001, 0.73413, 0.76068, 0.78027, 0.8, 0.82891, 0.85964, 0.89, 0.9127, 0.93664, 0.96677, 1.0, 1.02661, 1.04631, 1.05726, 1.06637, 1.06, 1.0)
@JvmField var N_W_SemiCirc: Int = 21
@JvmField var W_SemiCirc: DoubleArray? = doubleArrayOf(0.0, 0.5488, 0.8537, 1.0000, 1.0000, 0.9939, 0.9878, 0.9756, 0.9634, 0.9451, 0.9207, 0.8902, 0.8537, 0.8171, 0.7683, 0.7073, 0.6463, 0.5732, 0.4756, 0.3354, 0.0)
@JvmField var NumCodesEllipse: Int = 23
@JvmField var MinorAxis_Ellipse: DoubleArray? = doubleArrayOf(14.0, 19.0, 22.0, 24.0, 27.0, 29.0, 32.0, 34.0, 38.0, 43.0, 48.0, 53.0, 58.0, 63.0, 68.0, 72.0, 77.0, 82.0, 87.0, 92.0, 97.0, 106.0, 116.0)
@JvmField var MajorAxis_Ellipse: DoubleArray? = doubleArrayOf(23.0, 30.0, 34.0, 38.0, 42.0, 45.0, 49.0, 53.0, 60.0, 68.0, 76.0, 83.0, 91.0, 98.0, 106.0, 113.0, 121.0, 128.0, 136.0, 143.0, 151.0, 166.0, 180.0)
@JvmField var Afull_Ellipse: DoubleArray? = doubleArrayOf(1.80, 3.30, 4.10, 5.10, 6.30, 7.40, 8.80, 10.20, 12.90, 16.60, 20.50, 24.80, 29.50, 34.60, 40.10, 46.10, 52.40, 59.20, 66.40, 74.00, 82.00, 99.20, 118.60)
@JvmField var Rfull_Ellipse: DoubleArray? = doubleArrayOf(0.367, 0.490, 0.546, 0.613, 0.686, 0.736, 0.812, 0.875, 0.969, 1.106, 1.229, 1.352, 1.475, 1.598, 1.721, 1.845, 1.967, 2.091, 2.215, 2.340, 2.461, 2.707, 2.968)
@JvmField var N_A_HorizEllipse: Int = 26
@JvmField var A_HorizEllipse: DoubleArray? = doubleArrayOf(0.0000, 0.0150, 0.0400, 0.0650, 0.0950, 0.1300, 0.1650, 0.2050, 0.2500, 0.3000, 0.3550, 0.4150, 0.4800, 0.5200, 0.5850, 0.6450, 0.7000, 0.7500, 0.7950, 0.8350, 0.8700, 0.9050, 0.9350, 0.9600, 0.9850, 1.000)
@JvmField var N_R_HorizEllipse: Int = 26
@JvmField var R_HorizEllipse: DoubleArray? = doubleArrayOf(0.0100, 0.0764, 0.1726, 0.2389, 0.3274, 0.4191, 0.5120, 0.5983, 0.6757, 0.7630, 0.8326, 0.9114, 0.9702, 1.030, 1.091, 1.146, 1.185, 1.225, 1.257, 1.274, 1.290, 1.282, 1.274, 1.257, 1.185, 1.000)
@JvmField var N_W_HorizEllipse: Int = 26
@JvmField var W_HorizEllipse: DoubleArray? = doubleArrayOf(0.0, 0.3919, 0.5426, 0.6499, 0.7332, 0.8000, 0.8542, 0.8980, 0.9330, 0.9600, 0.9798, 0.9928, 0.9992, 0.9992, 0.9928, 0.9798, 0.9600, 0.9330, 0.8980, 0.8542, 0.8000, 0.7332, 0.6499, 0.5426, 0.3919, 0.0)
@JvmField var N_A_VertEllipse: Int = 26
@JvmField var A_VertEllipse: DoubleArray? = doubleArrayOf(0.0000, 0.0100, 0.0400, 0.0700, 0.1000, 0.1400, 0.1850, 0.2300, 0.2800, 0.3300, 0.3800, 0.4300, 0.4800, 0.5200, 0.5700, 0.6200, 0.6700, 0.7200, 0.7700, 0.8150, 0.8600, 0.9000, 0.9300, 0.9600, 0.9900, 1.000)
@JvmField var N_R_VertEllipse: Int = 26
@JvmField var R_VertEllipse: DoubleArray? = doubleArrayOf(0.0100, 0.1250, 0.2436, 0.3536, 0.4474, 0.5484, 0.6366, 0.7155, 0.7768, 0.8396, 0.8969, 0.9480, 0.9925, 1.023, 1.053, 1.084, 1.107, 1.130, 1.154, 1.170, 1.177, 1.177, 1.170, 1.162, 1.122, 1.000)
@JvmField var N_W_VertEllipse: Int = 26
@JvmField var W_VertEllipse: DoubleArray? = doubleArrayOf(0.0, 0.3919, 0.5426, 0.6499, 0.7332, 0.8000, 0.8542, 0.8980, 0.9330, 0.9600, 0.9798, 0.9928, 0.9992, 0.9992, 0.9928, 0.9798, 0.9600, 0.9330, 0.8980, 0.8542, 0.8000, 0.7332, 0.6499, 0.5426, 0.3919, 0.0)
@JvmField var NumCodesArch: Int = 102
@JvmField var Yfull_Arch: DoubleArray? = doubleArrayOf(11.0, 13.5, 15.5, 18.0, 22.5, 26.625, 31.3125, 36.0, 40.0, 45.0, 54.0, 62.0, 72.0, 77.5, 87.125, 96.875, 106.5, 13.0, 15.0, 18.0, 20.0, 24.0, 29.0, 33.0, 38.0, 43.0, 47.0, 52.0, 57.0, 31.0, 36.0, 41.0, 46.0, 51.0, 55.0, 59.0, 63.0, 67.0, 71.0, 75.0, 79.0, 83.0, 87.0, 91.0, 55.0, 57.0, 59.0, 61.0, 63.0, 65.0, 67.0, 69.0, 71.0, 73.0, 75.0, 77.0, 79.0, 81.0, 83.0, 85.0, 87.0, 89.0, 91.0, 93.0, 95.0, 97.0, 100.0, 101.0, 103.0, 105.0, 107.0, 109.0, 111.0, 113.0, 115.0, 118.0, 119.0, 121.0, 112.0, 114.0, 116.0, 118.0, 120.0, 122.0, 124.0, 126.0, 128.0, 130.0, 132.0, 134.0, 136.0, 138.0, 140.0, 142.0, 144.0, 146.0, 148.0, 150.0, 152.0, 154.0, 156.0, 158.0)
@JvmField var Wmax_Arch: DoubleArray? = doubleArrayOf(18.0, 22.0, 26.0, 28.5, 36.25, 43.75, 51.125, 58.5, 65.0, 73.0, 88.0, 102.0, 115.0, 122.0, 138.0, 154.0, 168.75, 17.0, 21.0, 24.0, 28.0, 35.0, 42.0, 49.0, 57.0, 64.0, 71.0, 77.0, 83.0, 40.0, 46.0, 53.0, 60.0, 66.0, 73.0, 81.0, 87.0, 95.0, 103.0, 112.0, 117.0, 128.0, 137.0, 142.0, 73.0, 76.0, 81.0, 84.0, 87.0, 92.0, 95.0, 98.0, 103.0, 106.0, 112.0, 114.0, 117.0, 123.0, 128.0, 131.0, 137.0, 139.0, 142.0, 148.0, 150.0, 152.0, 154.0, 161.0, 167.0, 169.0, 171.0, 178.0, 184.0, 186.0, 188.0, 190.0, 197.0, 199.0, 159.0, 162.0, 168.0, 170.0, 173.0, 179.0, 184.0, 187.0, 190.0, 195.0, 198.0, 204.0, 206.0, 209.0, 215.0, 217.0, 223.0, 225.0, 231.0, 234.0, 236.0, 239.0, 245.0, 247.0)
@JvmField var Afull_Arch: DoubleArray? = doubleArrayOf(1.1, 1.65, 2.2, 2.8, 4.4, 6.4, 8.8, 11.4, 14.3, 17.7, 25.6, 34.6, 44.5, 51.7, 66.0, 81.8, 99.1, 1.1, 1.6, 2.2, 2.9, 4.5, 6.5, 8.9, 11.6, 14.7, 18.1, 21.9, 26.0, 7.0, 9.4, 12.3, 15.6, 19.3, 23.2, 27.4, 32.1, 37.0, 42.4, 48.0, 54.2, 60.5, 67.4, 74.5, 22.0, 24.0, 26.0, 28.0, 31.0, 33.0, 35.0, 38.0, 40.0, 43.0, 46.0, 49.0, 52.0, 55.0, 58.0, 61.0, 64.0, 67.0, 71.0, 74.0, 78.0, 81.0, 85.0, 89.0, 93.0, 97.0, 101.0, 105.0, 109.0, 113.0, 118.0, 122.0, 126.0, 131.0, 97.0, 102.0, 105.0, 109.0, 114.0, 118.0, 123.0, 127.0, 132.0, 137.0, 142.0, 146.0, 151.0, 157.0, 161.0, 167.0, 172.0, 177.0, 182.0, 188.0, 194.0, 200.0, 205.0, 211.0)
@JvmField var Rfull_Arch: DoubleArray? = doubleArrayOf(0.25, 0.3, 0.36, 0.45, 0.56, 0.68, 0.8, 0.9, 1.01, 1.13, 1.35, 1.57, 1.77, 1.92, 2.17, 2.42, 2.65, 0.324, 0.374, 0.449, 0.499, 0.598, 0.723, 0.823, 0.947, 1.072, 1.171, 1.296, 1.421, 0.773, 0.773, 1.022, 1.147, 1.271, 1.371, 1.471, 1.570, 1.670, 1.770, 1.869, 1.969, 2.069, 2.168, 2.268, 1.371, 1.421, 1.471, 1.520, 1.570, 1.620, 1.670, 1.720, 1.770, 1.820, 1.869, 1.919, 1.969, 2.019, 2.069, 2.119, 2.168, 2.218, 2.268, 2.318, 2.368, 2.418, 2.493, 2.517, 2.567, 2.617, 2.667, 2.717, 2.767, 2.817, 2.866, 2.941, 2.966, 3.016, 2.792, 2.841, 2.891, 2.941, 2.991, 3.041, 3.091, 3.141, 3.190, 3.240, 3.290, 3.340, 3.390, 3.440, 3.490, 3.539, 3.589, 3.639, 3.689, 3.739, 3.789, 3.838, 3.888, 3.938)
@JvmField var N_A_Arch: Int = 26
@JvmField var A_Arch: DoubleArray? = doubleArrayOf(0.0000, 0.0200, 0.0600, 0.1000, 0.1400, 0.1900, 0.2400, 0.2900, 0.3400, 0.3900, 0.4400, 0.4900, 0.5400, 0.5900, 0.6400, 0.6900, 0.7350, 0.7800, 0.8200, 0.8600, 0.8950, 0.9300, 0.9600, 0.9850, 0.9950, 1.000)
@JvmField var N_R_Arch: Int = 26
@JvmField var R_Arch: DoubleArray? = doubleArrayOf(0.0100, 0.0983, 0.1965, 0.2948, 0.3940, 0.4962, 0.5911, 0.6796, 0.7615, 0.8364, 0.9044, 0.9640, 1.018, 1.065, 1.106, 1.142, 1.170, 1.192, 1.208, 1.217, 1.220, 1.213, 1.196, 1.168, 1.112, 1.000)
@JvmField var N_W_Arch: Int = 26
@JvmField var W_Arch: DoubleArray? = doubleArrayOf(0.0, 0.6272, 0.8521, 0.9243, 0.9645, 0.9846, 0.9964, 0.9988, 0.9917, 0.9811, 0.9680, 0.9515, 0.9314, 0.9101, 0.8864, 0.8592, 0.8284, 0.7917, 0.7527, 0.7065, 0.6544, 0.5953, 0.5231, 0.4355, 0.3195, 0.0)
@JvmField var Amax: DoubleArray? = doubleArrayOf(1.0, 0.9756, 0.9756, 0.97, 1.0, 1.0, 1.0, 1.0, 1.0, 0.98, 0.98, 0.96, 0.96, 0.96, 0.92, 0.96, 0.96, 0.96, 0.98, 0.98, 0.96, 0.96, 1.0, 0.96, 0.9756, 1.0)

// xsect.c:204
fun xsect_isOpen(type: Int): Int {
    return ((if (((Amax!![type] >= 1.0))) 1 else 0))
}

// xsect.c:216
fun xsect_setParams(xsect: TXsect?, type: Int, p: DoubleArray?, ucf: Double): Int {
    var index: Int = 0
    var aMax: Double = 0.0
    var theta: Double = 0.0
    if (((type != DUMMY) && (p!![0] <= 0.0))) {
        return 0
    }
    xsect!!.type = type
    when (xsect!!.type) {
        DUMMY -> {
            xsect!!.yFull = 1e-06
            xsect!!.wMax = 1e-06
            xsect!!.aFull = 1e-06
            xsect!!.rFull = 1e-06
            xsect!!.sFull = 1e-06
            xsect!!.sMax = 1e-06
        }
        CIRCULAR -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = xsect!!.yFull
            xsect!!.aFull = (((3.141592654 / 4.0) * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.2500 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.08 * xsect!!.sFull)
            xsect!!.ywMax = (0.5 * xsect!!.yFull)
        }
        FORCE_MAIN -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = xsect!!.yFull
            xsect!!.aFull = (((3.141592654 / 4.0) * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.2500 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, 0.63))
            xsect!!.sMax = (1.06949 * xsect!!.sFull)
            xsect!!.ywMax = (0.5 * xsect!!.yFull)
            xsect!!.rBot = p!![1]
        }
        FILLED_CIRCULAR -> {
            if ((p!![1] >= p!![0])) {
                return 0
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = xsect!!.yFull
            xsect!!.aFull = (((3.141592654 / 4.0) * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.2500 * xsect!!.yFull)
            xsect!!.yBot = (p!![1] / ucf)
            xsect!!.aBot = circ_getAofY(xsect, xsect!!.yBot)
            xsect!!.sBot = xsect_getWofY(xsect, xsect!!.yBot)
            xsect!!.rBot = (xsect!!.aBot / ((xsect!!.rFull * lookup((xsect!!.yBot / xsect!!.yFull), R_Circ, N_R_Circ))))
            xsect!!.aFull -= xsect!!.aBot
            xsect!!.rFull = (xsect!!.aFull / ((((3.141592654 * xsect!!.yFull) - xsect!!.rBot) + xsect!!.sBot)))
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.08 * xsect!!.sFull)
            xsect!!.yFull -= xsect!!.yBot
            xsect!!.ywMax = (0.5 * xsect!!.yFull)
        }
        EGGSHAPED -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.aFull = ((0.5105 * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.1931 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.065 * xsect!!.sFull)
            xsect!!.wMax = ((2.0 / 3.0) * xsect!!.yFull)
            xsect!!.ywMax = (0.64 * xsect!!.yFull)
        }
        HORSESHOE -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.aFull = ((0.8293 * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.2538 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.077 * xsect!!.sFull)
            xsect!!.wMax = (1.0 * xsect!!.yFull)
            xsect!!.ywMax = (0.5 * xsect!!.yFull)
        }
        GOTHIC -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.aFull = ((0.6554 * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.2269 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.065 * xsect!!.sFull)
            xsect!!.wMax = (0.84 * xsect!!.yFull)
            xsect!!.ywMax = (0.45 * xsect!!.yFull)
        }
        CATENARY -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.aFull = ((0.70277 * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.23172 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.05 * xsect!!.sFull)
            xsect!!.wMax = (0.9 * xsect!!.yFull)
            xsect!!.ywMax = (0.25 * xsect!!.yFull)
        }
        SEMIELLIPTICAL -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.aFull = ((0.785 * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.242 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.045 * xsect!!.sFull)
            xsect!!.wMax = (1.0 * xsect!!.yFull)
            xsect!!.ywMax = (0.15 * xsect!!.yFull)
        }
        BASKETHANDLE -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.aFull = ((0.7862 * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.2464 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.06078 * xsect!!.sFull)
            xsect!!.wMax = (0.944 * xsect!!.yFull)
            xsect!!.ywMax = (0.2 * xsect!!.yFull)
        }
        SEMICIRCULAR -> {
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.aFull = ((1.2697 * xsect!!.yFull) * xsect!!.yFull)
            xsect!!.rFull = (0.2946 * xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = (1.06637 * xsect!!.sFull)
            xsect!!.wMax = (1.64 * xsect!!.yFull)
            xsect!!.ywMax = (0.15 * xsect!!.yFull)
        }
        RECT_CLOSED -> {
            if ((p!![1] <= 0.0)) {
                return 0
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = (p!![1] / ucf)
            xsect!!.aFull = (xsect!!.yFull * xsect!!.wMax)
            xsect!!.rFull = (xsect!!.aFull / ((2.0 * ((xsect!!.yFull + xsect!!.wMax)))))
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            aMax = (0.97 * xsect!!.aFull)
            xsect!!.sMax = (aMax * Glibm.pow(rect_closed_getRofA(xsect, aMax), (2.0 / 3.0)))
            xsect!!.ywMax = xsect!!.yFull
        }
        RECT_OPEN -> {
            if ((p!![1] <= 0.0)) {
                return 0
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = (p!![1] / ucf)
            if (((p!![2] < 0.0) || (p!![2] > 2.0))) {
                return 0
            }
            xsect!!.sBot = p!![2]
            xsect!!.aFull = (xsect!!.yFull * xsect!!.wMax)
            xsect!!.rFull = (xsect!!.aFull / (((((2.0 - xsect!!.sBot)) * xsect!!.yFull) + xsect!!.wMax)))
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect!!.sFull
            xsect!!.ywMax = xsect!!.yFull
        }
        RECT_TRIANG -> {
            if (((p!![1] <= 0.0) || (p!![2] <= 0.0))) {
                return 0
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = (p!![1] / ucf)
            xsect!!.yBot = (p!![2] / ucf)
            xsect!!.ywMax = xsect!!.yFull
            xsect!!.aBot = ((xsect!!.yBot * xsect!!.wMax) / 2.0)
            xsect!!.sBot = ((xsect!!.wMax / xsect!!.yBot) / 2.0)
            xsect!!.rBot = sqrt((1.0 + (xsect!!.sBot * xsect!!.sBot)))
            xsect!!.aFull = (xsect!!.wMax * ((xsect!!.yFull - (xsect!!.yBot / 2.0))))
            xsect!!.rFull = (xsect!!.aFull / (((((2.0 * xsect!!.yBot) * xsect!!.rBot) + (2.0 * ((xsect!!.yFull - xsect!!.yBot)))) + xsect!!.wMax)))
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            aMax = (0.98 * xsect!!.aFull)
            xsect!!.sMax = (aMax * Glibm.pow(rect_triang_getRofA(xsect, aMax), (2.0 / 3.0)))
        }
        RECT_ROUND -> {
            if ((p!![1] <= 0.0)) {
                return 0
            }
            if ((p!![2] < (p!![1] / 2.0))) {
                p!![2] = (p!![1] / 2.0)
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = (p!![1] / ucf)
            xsect!!.rBot = (p!![2] / ucf)
            theta = (2.0 * Glibm.asin(((xsect!!.wMax / 2.0) / xsect!!.rBot)))
            xsect!!.aBot = (((xsect!!.rBot * xsect!!.rBot) / 2.0) * ((theta - Glibm.sin(theta))))
            xsect!!.sBot = (((3.141592654 * xsect!!.rBot) * xsect!!.rBot) * Glibm.pow((xsect!!.rBot / 2.0), (2.0 / 3.0)))
            xsect!!.yBot = (xsect!!.rBot * ((1.0 - Glibm.cos((theta / 2.0)))))
            if ((xsect!!.yBot > xsect!!.yFull)) {
                return 0
            }
            xsect!!.ywMax = xsect!!.yFull
            xsect!!.aFull = ((xsect!!.wMax * ((xsect!!.yFull - xsect!!.yBot))) + xsect!!.aBot)
            xsect!!.rFull = (xsect!!.aFull / ((((xsect!!.rBot * theta) + (2.0 * ((xsect!!.yFull - xsect!!.yBot)))) + xsect!!.wMax)))
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            aMax = (0.98 * xsect!!.aFull)
            xsect!!.sMax = (aMax * Glibm.pow(rect_round_getRofA(xsect, aMax), (2.0 / 3.0)))
        }
        MOD_BASKET -> {
            if ((p!![1] <= 0.0)) {
                return 0
            }
            if ((p!![2] < (p!![1] / 2.0))) {
                p!![2] = (p!![1] / 2.0)
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = (p!![1] / ucf)
            xsect!!.rBot = (p!![2] / ucf)
            theta = (2.0 * Glibm.asin(((xsect!!.wMax / 2.0) / xsect!!.rBot)))
            xsect!!.sBot = theta
            xsect!!.yBot = (xsect!!.rBot * ((1.0 - Glibm.cos((theta / 2.0)))))
            if ((xsect!!.yBot > xsect!!.yFull)) {
                return 0
            }
            xsect!!.ywMax = (xsect!!.yFull - xsect!!.yBot)
            xsect!!.aBot = (((xsect!!.rBot * xsect!!.rBot) / 2.0) * ((theta - Glibm.sin(theta))))
            xsect!!.aFull = ((((xsect!!.yFull - xsect!!.yBot)) * xsect!!.wMax) + xsect!!.aBot)
            xsect!!.rFull = (xsect!!.aFull / ((((xsect!!.rBot * theta) + (2.0 * ((xsect!!.yFull - xsect!!.yBot)))) + xsect!!.wMax)))
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect_getSofA(xsect, (Amax!![MOD_BASKET] * xsect!!.aFull))
        }
        TRAPEZOIDAL -> {
            if ((((p!![1] < 0.0) || (p!![2] < 0.0)) || (p!![3] < 0.0))) {
                return 0
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.ywMax = xsect!!.yFull
            xsect!!.yBot = (p!![1] / ucf)
            xsect!!.sBot = (((p!![2] + p!![3])) / 2.0)
            if (((xsect!!.yBot == 0.0) && (xsect!!.sBot == 0.0))) {
                return 0
            }
            xsect!!.rBot = (sqrt((1.0 + (p!![2] * p!![2]))) + sqrt((1.0 + (p!![3] * p!![3]))))
            xsect!!.wMax = (xsect!!.yBot + (xsect!!.yFull * ((p!![2] + p!![3]))))
            xsect!!.aFull = (((xsect!!.yBot + (xsect!!.sBot * xsect!!.yFull))) * xsect!!.yFull)
            xsect!!.rFull = (xsect!!.aFull / ((xsect!!.yBot + (xsect!!.yFull * xsect!!.rBot))))
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect!!.sFull
        }
        TRIANGULAR -> {
            if ((p!![1] <= 0.0)) {
                return 0
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = (p!![1] / ucf)
            xsect!!.ywMax = xsect!!.yFull
            xsect!!.sBot = ((xsect!!.wMax / xsect!!.yFull) / 2.0)
            xsect!!.rBot = sqrt((1.0 + (xsect!!.sBot * xsect!!.sBot)))
            xsect!!.aFull = ((xsect!!.yFull * xsect!!.yFull) * xsect!!.sBot)
            xsect!!.rFull = (xsect!!.aFull / (((2.0 * xsect!!.yFull) * xsect!!.rBot)))
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect!!.sFull
        }
        PARABOLIC -> {
            if ((p!![1] <= 0.0)) {
                return 0
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = (p!![1] / ucf)
            xsect!!.ywMax = xsect!!.yFull
            xsect!!.rBot = ((xsect!!.wMax / 2.0) / sqrt(xsect!!.yFull))
            xsect!!.aFull = ((((2.0 / 3.0)) * xsect!!.yFull) * xsect!!.wMax)
            xsect!!.rFull = xsect_getRofY(xsect, xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect!!.sFull
        }
        POWERFUNC -> {
            if (((p!![1] <= 0.0) || (p!![2] <= 0.0))) {
                return 0
            }
            xsect!!.yFull = (p!![0] / ucf)
            xsect!!.wMax = (p!![1] / ucf)
            xsect!!.ywMax = xsect!!.yFull
            xsect!!.sBot = (1.0 / p!![2])
            xsect!!.rBot = ((xsect!!.wMax / ((xsect!!.sBot + 1.0))) / Glibm.pow(xsect!!.yFull, xsect!!.sBot))
            xsect!!.aFull = ((xsect!!.yFull * xsect!!.wMax) / ((xsect!!.sBot + 1.0)))
            xsect!!.rFull = xsect_getRofY(xsect, xsect!!.yFull)
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect!!.sFull
        }
        HORIZ_ELLIPSE -> {
            if ((p!![1] == 0.0)) {
                p!![2] = p!![0]
            }
            if ((p!![2] > 0.0)) {
                index = (R.D2I(floor(p!![2])) - 1)
                if (((index < 0) || (index >= NumCodesEllipse))) {
                    return 0
                }
                xsect!!.yFull = (MinorAxis_Ellipse!![index] / 12.0)
                xsect!!.wMax = (MajorAxis_Ellipse!![index] / 12.0)
                xsect!!.aFull = Afull_Ellipse!![index]
                xsect!!.rFull = Rfull_Ellipse!![index]
            } else {
                xsect!!.yFull = (p!![0] / ucf)
                if ((p!![1] < 0.0)) {
                    return 0
                }
                xsect!!.wMax = (p!![1] / ucf)
                xsect!!.aFull = ((1.2692 * xsect!!.yFull) * xsect!!.yFull)
                xsect!!.rFull = (0.3061 * xsect!!.yFull)
            }
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect!!.sFull
            xsect!!.ywMax = (0.48 * xsect!!.yFull)
        }
        VERT_ELLIPSE -> {
            if ((p!![1] == 0.0)) {
                p!![2] = p!![0]
            }
            if ((p!![2] > 0.0)) {
                index = (R.D2I(floor(p!![2])) - 1)
                if (((index < 0) || (index >= NumCodesEllipse))) {
                    return 0
                }
                xsect!!.yFull = (MajorAxis_Ellipse!![index] / 12.0)
                xsect!!.wMax = (MinorAxis_Ellipse!![index] / 12.0)
                xsect!!.aFull = Afull_Ellipse!![index]
                xsect!!.rFull = Rfull_Ellipse!![index]
            } else {
                if ((p!![1] < 0.0)) {
                    return 0
                }
                xsect!!.yFull = (p!![0] / ucf)
                xsect!!.wMax = (p!![1] / ucf)
                xsect!!.aFull = ((1.2692 * xsect!!.wMax) * xsect!!.wMax)
                xsect!!.rFull = (0.3061 * xsect!!.wMax)
            }
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect!!.sFull
            xsect!!.ywMax = (0.48 * xsect!!.yFull)
        }
        ARCH -> {
            if ((p!![1] == 0.0)) {
                p!![2] = p!![0]
            }
            if ((p!![2] > 0.0)) {
                index = (R.D2I(floor(p!![2])) - 1)
                if (((index < 0) || (index >= NumCodesArch))) {
                    return 0
                }
                xsect!!.yFull = (Yfull_Arch!![index] / 12.0)
                xsect!!.wMax = (Wmax_Arch!![index] / 12.0)
                xsect!!.aFull = Afull_Arch!![index]
                xsect!!.rFull = Rfull_Arch!![index]
            } else {
                if ((p!![1] < 0.0)) {
                    return 0
                }
                xsect!!.yFull = (p!![0] / ucf)
                xsect!!.wMax = (p!![1] / ucf)
                xsect!!.aFull = ((0.7879 * xsect!!.yFull) * xsect!!.wMax)
                xsect!!.rFull = (0.2991 * xsect!!.yFull)
            }
            xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
            xsect!!.sMax = xsect!!.sFull
            xsect!!.ywMax = (0.28 * xsect!!.yFull)
        }
    }
    return 1
}

// xsect.c:638
fun xsect_setIrregXsectParams(xsect: TXsect?) {
    var index: Int = xsect!!.transect
    getTransectParams(xsect, Transect!![index])
}

// xsect.c:651
fun xsect_setStreetXsectParams(xsect: TXsect?) {
    var index: Int = xsect!!.transect
    getTransectParams(xsect, Street!![index]!!.transect)
}

// xsect.c:664
fun xsect_setCustomXsectParams(xsect: TXsect?) {
    var index: Int = Curve!![xsect!!.transect]!!.refersTo
    var yFull: Double = xsect!!.yFull
    var i: Int = 0
    var iMax: Int = 0
    var wMax: Double = 0.0
    var wTbl: DoubleArray? = Shape!![index]!!.widthTbl
    xsect!!.wMax = (Shape!![index]!!.wMax * yFull)
    xsect!!.aFull = ((Shape!![index]!!.aFull * yFull) * yFull)
    xsect!!.rFull = (Shape!![index]!!.rFull * yFull)
    xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
    xsect!!.sMax = (((Shape!![index]!!.sMax * yFull) * yFull) * Glibm.pow(yFull, (2.0 / 3.0)))
    xsect!!.aBot = ((Shape!![index]!!.aMax * yFull) * yFull)
    iMax = 0
    wMax = wTbl!![0]
    i = 1
    f1@ while ((i < 51)) {
        if ((wTbl!![i] < wMax)) {
            break@f1
        }
        wMax = wTbl!![i]
        iMax = i
        i++
    }
    xsect!!.ywMax = ((yFull * (iMax).toDouble()) / (((51 - 1))).toDouble())
}

// xsect.c:700
fun xsect_getAmax(xsect: TXsect?): Double {
    if ((xsect!!.type == IRREGULAR)) {
        return xsect!!.aBot
    } else if ((xsect!!.type == CUSTOM)) {
        return xsect!!.aBot
    } else {
        return (Amax!![xsect!!.type] * xsect!!.aFull)
    }
    throw IllegalStateException("xsect_getAmax: no return")
}

// xsect.c:714
fun xsect_getSofA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = (a / xsect!!.aFull)
    var r: Double = 0.0
    when (xsect!!.type) {
        FORCE_MAIN, CIRCULAR -> {
            return circ_getSofA(xsect, a)
        }
        EGGSHAPED -> {
            return (xsect!!.sFull * lookup(alpha, S_Egg, N_S_Egg))
        }
        HORSESHOE -> {
            return (xsect!!.sFull * lookup(alpha, S_Horseshoe, N_S_Horseshoe))
        }
        GOTHIC -> {
            return (xsect!!.sFull * lookup(alpha, S_Gothic, N_S_Gothic))
        }
        CATENARY -> {
            return (xsect!!.sFull * lookup(alpha, S_Catenary, N_S_Catenary))
        }
        SEMIELLIPTICAL -> {
            return (xsect!!.sFull * lookup(alpha, S_SemiEllip, N_S_SemiEllip))
        }
        BASKETHANDLE -> {
            return (xsect!!.sFull * lookup(alpha, S_BasketHandle, N_S_BasketHandle))
        }
        SEMICIRCULAR -> {
            return (xsect!!.sFull * lookup(alpha, S_SemiCirc, N_S_SemiCirc))
        }
        RECT_CLOSED -> {
            return rect_closed_getSofA(xsect, a)
        }
        RECT_OPEN -> {
            return rect_open_getSofA(xsect, a)
        }
        RECT_TRIANG -> {
            return rect_triang_getSofA(xsect, a)
        }
        RECT_ROUND -> {
            return rect_round_getSofA(xsect, a)
        }
        else -> {
            if ((a == 0.0)) {
                return 0.0
            }
            r = xsect_getRofA(xsect, a)
            if ((r < 1e-06)) {
                return 0.0
            }
            return (a * Glibm.pow(r, (2.0 / 3.0)))
        }
    }
    throw IllegalStateException("xsect_getSofA: no return")
}

// xsect.c:773
fun xsect_getYofA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = (a / xsect!!.aFull)
    when (xsect!!.type) {
        FORCE_MAIN, CIRCULAR -> {
            return circ_getYofA(xsect, a)
        }
        FILLED_CIRCULAR -> {
            return filled_circ_getYofA(xsect, a)
        }
        EGGSHAPED -> {
            return (xsect!!.yFull * lookup(alpha, Y_Egg, N_Y_Egg))
        }
        HORSESHOE -> {
            return (xsect!!.yFull * lookup(alpha, Y_Horseshoe, N_Y_Horseshoe))
        }
        GOTHIC -> {
            return (xsect!!.yFull * lookup(alpha, Y_Gothic, N_Y_Gothic))
        }
        CATENARY -> {
            return (xsect!!.yFull * lookup(alpha, Y_Catenary, N_Y_Catenary))
        }
        SEMIELLIPTICAL -> {
            return (xsect!!.yFull * lookup(alpha, Y_SemiEllip, N_Y_SemiEllip))
        }
        BASKETHANDLE -> {
            return (xsect!!.yFull * lookup(alpha, Y_BasketHandle, N_Y_BasketHandle))
        }
        SEMICIRCULAR -> {
            return (xsect!!.yFull * lookup(alpha, Y_SemiCirc, N_Y_SemiCirc))
        }
        HORIZ_ELLIPSE -> {
            return (xsect!!.yFull * invLookup(alpha, A_HorizEllipse, N_A_HorizEllipse))
        }
        VERT_ELLIPSE -> {
            return (xsect!!.yFull * invLookup(alpha, A_VertEllipse, N_A_VertEllipse))
        }
        IRREGULAR -> {
            return (xsect!!.yFull * invLookup(alpha, Transect!![xsect!!.transect]!!.areaTbl, 51))
        }
        CUSTOM -> {
            return (xsect!!.yFull * invLookup(alpha, Shape!![Curve!![xsect!!.transect]!!.refersTo]!!.areaTbl, 51))
        }
        STREET_XSECT -> {
            return (xsect!!.yFull * invLookup(alpha, Street!![xsect!!.transect]!!.transect!!.areaTbl, Street!![xsect!!.transect]!!.transect!!.nTbl))
        }
        ARCH -> {
            return (xsect!!.yFull * invLookup(alpha, A_Arch, N_A_Arch))
        }
        RECT_CLOSED -> {
            return (a / xsect!!.wMax)
        }
        RECT_TRIANG -> {
            return rect_triang_getYofA(xsect, a)
        }
        RECT_ROUND -> {
            return rect_round_getYofA(xsect, a)
        }
        RECT_OPEN -> {
            return (a / xsect!!.wMax)
        }
        MOD_BASKET -> {
            return mod_basket_getYofA(xsect, a)
        }
        TRAPEZOIDAL -> {
            return trapez_getYofA(xsect, a)
        }
        TRIANGULAR -> {
            return triang_getYofA(xsect, a)
        }
        PARABOLIC -> {
            return parab_getYofA(xsect, a)
        }
        POWERFUNC -> {
            return powerfunc_getYofA(xsect, a)
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("xsect_getYofA: no return")
}

// xsect.c:857
fun xsect_getAofY(xsect: TXsect?, y: Double): Double {
    var yNorm: Double = (y / xsect!!.yFull)
    if ((y <= 0.0)) {
        return 0.0
    }
    when (xsect!!.type) {
        FORCE_MAIN, CIRCULAR -> {
            return (xsect!!.aFull * lookup(yNorm, A_Circ, N_A_Circ))
        }
        FILLED_CIRCULAR -> {
            return filled_circ_getAofY(xsect, y)
        }
        EGGSHAPED -> {
            return (xsect!!.aFull * lookup(yNorm, A_Egg, N_A_Egg))
        }
        HORSESHOE -> {
            return (xsect!!.aFull * lookup(yNorm, A_Horseshoe, N_A_Horseshoe))
        }
        GOTHIC -> {
            return (xsect!!.aFull * invLookup(yNorm, Y_Gothic, N_Y_Gothic))
        }
        CATENARY -> {
            return (xsect!!.aFull * invLookup(yNorm, Y_Catenary, N_Y_Catenary))
        }
        SEMIELLIPTICAL -> {
            return (xsect!!.aFull * invLookup(yNorm, Y_SemiEllip, N_Y_SemiEllip))
        }
        BASKETHANDLE -> {
            return (xsect!!.aFull * lookup(yNorm, A_Baskethandle, N_A_Baskethandle))
        }
        SEMICIRCULAR -> {
            return (xsect!!.aFull * invLookup(yNorm, Y_SemiCirc, N_Y_SemiCirc))
        }
        HORIZ_ELLIPSE -> {
            return (xsect!!.aFull * lookup(yNorm, A_HorizEllipse, N_A_HorizEllipse))
        }
        VERT_ELLIPSE -> {
            return (xsect!!.aFull * lookup(yNorm, A_VertEllipse, N_A_VertEllipse))
        }
        ARCH -> {
            return (xsect!!.aFull * lookup(yNorm, A_Arch, N_A_Arch))
        }
        IRREGULAR -> {
            return (xsect!!.aFull * lookup(yNorm, Transect!![xsect!!.transect]!!.areaTbl, 51))
        }
        CUSTOM -> {
            return (xsect!!.aFull * lookup(yNorm, Shape!![Curve!![xsect!!.transect]!!.refersTo]!!.areaTbl, 51))
        }
        STREET_XSECT -> {
            return (xsect!!.aFull * lookup(yNorm, Street!![xsect!!.transect]!!.transect!!.areaTbl, Street!![xsect!!.transect]!!.transect!!.nTbl))
        }
        RECT_CLOSED -> {
            return (y * xsect!!.wMax)
        }
        RECT_TRIANG -> {
            return rect_triang_getAofY(xsect, y)
        }
        RECT_ROUND -> {
            return rect_round_getAofY(xsect, y)
        }
        RECT_OPEN -> {
            return (y * xsect!!.wMax)
        }
        MOD_BASKET -> {
            return mod_basket_getAofY(xsect, y)
        }
        TRAPEZOIDAL -> {
            return trapez_getAofY(xsect, y)
        }
        TRIANGULAR -> {
            return triang_getAofY(xsect, y)
        }
        PARABOLIC -> {
            return parab_getAofY(xsect, y)
        }
        POWERFUNC -> {
            return powerfunc_getAofY(xsect, y)
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("xsect_getAofY: no return")
}

// xsect.c:943
fun xsect_getWofY(xsect: TXsect?, y: Double): Double {
    var yNorm: Double = (y / xsect!!.yFull)
    when (xsect!!.type) {
        FORCE_MAIN, CIRCULAR -> {
            return (xsect!!.wMax * lookup(yNorm, W_Circ, N_W_Circ))
        }
        FILLED_CIRCULAR -> {
            yNorm = (((y + xsect!!.yBot)) / ((xsect!!.yFull + xsect!!.yBot)))
            return (xsect!!.wMax * lookup(yNorm, W_Circ, N_W_Circ))
        }
        EGGSHAPED -> {
            return (xsect!!.wMax * lookup(yNorm, W_Egg, N_W_Egg))
        }
        HORSESHOE -> {
            return (xsect!!.wMax * lookup(yNorm, W_Horseshoe, N_W_Horseshoe))
        }
        GOTHIC -> {
            return (xsect!!.wMax * lookup(yNorm, W_Gothic, N_W_Gothic))
        }
        CATENARY -> {
            return (xsect!!.wMax * lookup(yNorm, W_Catenary, N_W_Catenary))
        }
        SEMIELLIPTICAL -> {
            return (xsect!!.wMax * lookup(yNorm, W_SemiEllip, N_W_SemiEllip))
        }
        BASKETHANDLE -> {
            return (xsect!!.wMax * lookup(yNorm, W_BasketHandle, N_W_BasketHandle))
        }
        SEMICIRCULAR -> {
            return (xsect!!.wMax * lookup(yNorm, W_SemiCirc, N_W_SemiCirc))
        }
        HORIZ_ELLIPSE -> {
            return (xsect!!.wMax * lookup(yNorm, W_HorizEllipse, N_W_HorizEllipse))
        }
        VERT_ELLIPSE -> {
            return (xsect!!.wMax * lookup(yNorm, W_VertEllipse, N_W_VertEllipse))
        }
        ARCH -> {
            return (xsect!!.wMax * lookup(yNorm, W_Arch, N_W_Arch))
        }
        IRREGULAR -> {
            return (xsect!!.wMax * lookup(yNorm, Transect!![xsect!!.transect]!!.widthTbl, 51))
        }
        CUSTOM -> {
            return (xsect!!.wMax * lookup(yNorm, Shape!![Curve!![xsect!!.transect]!!.refersTo]!!.widthTbl, 51))
        }
        STREET_XSECT -> {
            return (xsect!!.wMax * lookup(yNorm, Street!![xsect!!.transect]!!.transect!!.widthTbl, Street!![xsect!!.transect]!!.transect!!.nTbl))
        }
        RECT_CLOSED -> {
            if ((yNorm == 1.0)) {
                return 0.0
            }
            return xsect!!.wMax
        }
        RECT_TRIANG -> {
            return rect_triang_getWofY(xsect, y)
        }
        RECT_ROUND -> {
            return rect_round_getWofY(xsect, y)
        }
        RECT_OPEN -> {
            return xsect!!.wMax
        }
        MOD_BASKET -> {
            return mod_basket_getWofY(xsect, y)
        }
        TRAPEZOIDAL -> {
            return trapez_getWofY(xsect, y)
        }
        TRIANGULAR -> {
            return triang_getWofY(xsect, y)
        }
        PARABOLIC -> {
            return parab_getWofY(xsect, y)
        }
        POWERFUNC -> {
            return powerfunc_getWofY(xsect, y)
        }
        else -> {
            return 0.0
        }
    }
    throw IllegalStateException("xsect_getWofY: no return")
}

// xsect.c:1031
fun xsect_getRofY(xsect: TXsect?, y: Double): Double {
    var yNorm: Double = (y / xsect!!.yFull)
    when (xsect!!.type) {
        FORCE_MAIN, CIRCULAR -> {
            return (xsect!!.rFull * lookup(yNorm, R_Circ, N_R_Circ))
        }
        FILLED_CIRCULAR -> {
            if ((xsect!!.yBot == 0.0)) {
                return (xsect!!.rFull * lookup(yNorm, R_Circ, N_R_Circ))
            }
            return filled_circ_getRofY(xsect, y)
        }
        EGGSHAPED -> {
            return (xsect!!.rFull * lookup(yNorm, R_Egg, N_R_Egg))
        }
        HORSESHOE -> {
            return (xsect!!.rFull * lookup(yNorm, R_Horseshoe, N_R_Horseshoe))
        }
        BASKETHANDLE -> {
            return (xsect!!.rFull * lookup(yNorm, R_Baskethandle, N_R_Baskethandle))
        }
        HORIZ_ELLIPSE -> {
            return (xsect!!.rFull * lookup(yNorm, R_HorizEllipse, N_R_HorizEllipse))
        }
        VERT_ELLIPSE -> {
            return (xsect!!.rFull * lookup(yNorm, R_VertEllipse, N_R_VertEllipse))
        }
        ARCH -> {
            return (xsect!!.rFull * lookup(yNorm, R_Arch, N_R_Arch))
        }
        IRREGULAR -> {
            return (xsect!!.rFull * lookup(yNorm, Transect!![xsect!!.transect]!!.hradTbl, 51))
        }
        CUSTOM -> {
            return (xsect!!.rFull * lookup(yNorm, Shape!![Curve!![xsect!!.transect]!!.refersTo]!!.hradTbl, 51))
        }
        STREET_XSECT -> {
            return (xsect!!.rFull * lookup(yNorm, Street!![xsect!!.transect]!!.transect!!.hradTbl, Street!![xsect!!.transect]!!.transect!!.nTbl))
        }
        RECT_TRIANG -> {
            return rect_triang_getRofY(xsect, y)
        }
        RECT_ROUND -> {
            return rect_round_getRofY(xsect, y)
        }
        TRAPEZOIDAL -> {
            return trapez_getRofY(xsect, y)
        }
        TRIANGULAR -> {
            return triang_getRofY(xsect, y)
        }
        PARABOLIC -> {
            return parab_getRofY(xsect, y)
        }
        POWERFUNC -> {
            return powerfunc_getRofY(xsect, y)
        }
        else -> {
            return xsect_getRofA(xsect, xsect_getAofY(xsect, y))
        }
    }
    throw IllegalStateException("xsect_getRofY: no return")
}

// xsect.c:1100
fun xsect_getRofA(xsect: TXsect?, a: Double): Double {
    var cathy: Double = 0.0
    if ((a <= 0.0)) {
        return 0.0
    }
    when (xsect!!.type) {
        HORIZ_ELLIPSE, VERT_ELLIPSE, ARCH, IRREGULAR, FILLED_CIRCULAR, CUSTOM, STREET_XSECT -> {
            return xsect_getRofY(xsect, xsect_getYofA(xsect, a))
        }
        RECT_CLOSED -> {
            return rect_closed_getRofA(xsect, a)
        }
        RECT_OPEN -> {
            return (a / ((xsect!!.wMax + ((((2.0 - xsect!!.sBot)) * a) / xsect!!.wMax))))
        }
        RECT_TRIANG -> {
            return rect_triang_getRofA(xsect, a)
        }
        RECT_ROUND -> {
            return rect_round_getRofA(xsect, a)
        }
        MOD_BASKET -> {
            return mod_basket_getRofA(xsect, a)
        }
        TRAPEZOIDAL -> {
            return trapez_getRofA(xsect, a)
        }
        TRIANGULAR -> {
            return triang_getRofA(xsect, a)
        }
        PARABOLIC -> {
            return parab_getRofA(xsect, a)
        }
        POWERFUNC -> {
            return powerfunc_getRofA(xsect, a)
        }
        else -> {
            cathy = xsect_getSofA(xsect, a)
            if (((cathy < 1e-06) || (a < 1e-06))) {
                return 0.0
            }
            return Glibm.pow((cathy / a), (3.0 / 2.0))
        }
    }
    throw IllegalStateException("xsect_getRofA: no return")
}

// xsect.c:1149
fun xsect_getAofS(xsect: TXsect?, __s: Double): Double {
    var s: Double = __s
    var psi: Double = (s / xsect!!.sFull)
    if ((s <= 0.0)) {
        return 0.0
    }
    if ((s > xsect!!.sMax)) {
        s = xsect!!.sMax
    }
    when (xsect!!.type) {
        DUMMY -> {
            return 0.0
        }
        FORCE_MAIN, CIRCULAR -> {
            return circ_getAofS(xsect, s)
        }
        EGGSHAPED -> {
            return (xsect!!.aFull * invLookup(psi, S_Egg, N_S_Egg))
        }
        HORSESHOE -> {
            return (xsect!!.aFull * invLookup(psi, S_Horseshoe, N_S_Horseshoe))
        }
        GOTHIC -> {
            return (xsect!!.aFull * invLookup(psi, S_Gothic, N_S_Gothic))
        }
        CATENARY -> {
            return (xsect!!.aFull * invLookup(psi, S_Catenary, N_S_Catenary))
        }
        SEMIELLIPTICAL -> {
            return (xsect!!.aFull * invLookup(psi, S_SemiEllip, N_S_SemiEllip))
        }
        BASKETHANDLE -> {
            return (xsect!!.aFull * invLookup(psi, S_BasketHandle, N_S_BasketHandle))
        }
        SEMICIRCULAR -> {
            return (xsect!!.aFull * invLookup(psi, S_SemiCirc, N_S_SemiCirc))
        }
        else -> {
            return generic_getAofS(xsect, s)
        }
    }
    throw IllegalStateException("xsect_getAofS: no return")
}

// xsect.c:1194
fun xsect_getdSdA(xsect: TXsect?, a: Double): Double {
    when (xsect!!.type) {
        FORCE_MAIN, CIRCULAR -> {
            return circ_getdSdA(xsect, a)
        }
        EGGSHAPED -> {
            return tabular_getdSdA(xsect, a, S_Egg, N_S_Egg)
        }
        HORSESHOE -> {
            return tabular_getdSdA(xsect, a, S_Horseshoe, N_S_Horseshoe)
        }
        GOTHIC -> {
            return tabular_getdSdA(xsect, a, S_Gothic, N_S_Gothic)
        }
        CATENARY -> {
            return tabular_getdSdA(xsect, a, S_Catenary, N_S_Catenary)
        }
        SEMIELLIPTICAL -> {
            return tabular_getdSdA(xsect, a, S_SemiEllip, N_S_SemiEllip)
        }
        BASKETHANDLE -> {
            return tabular_getdSdA(xsect, a, S_BasketHandle, N_S_BasketHandle)
        }
        SEMICIRCULAR -> {
            return tabular_getdSdA(xsect, a, S_SemiCirc, N_S_SemiCirc)
        }
        RECT_CLOSED -> {
            return rect_closed_getdSdA(xsect, a)
        }
        RECT_OPEN -> {
            return rect_open_getdSdA(xsect, a)
        }
        RECT_TRIANG -> {
            return rect_triang_getdSdA(xsect, a)
        }
        RECT_ROUND -> {
            return rect_round_getdSdA(xsect, a)
        }
        MOD_BASKET -> {
            return mod_basket_getdSdA(xsect, a)
        }
        TRAPEZOIDAL -> {
            return trapez_getdSdA(xsect, a)
        }
        TRIANGULAR -> {
            return triang_getdSdA(xsect, a)
        }
        else -> {
            return generic_getdSdA(xsect, a)
        }
    }
    throw IllegalStateException("xsect_getdSdA: no return")
}

// xsect.c:1257
fun xsect_getYcrit(xsect: TXsect?, q: Double): Double {
    var q2g: Double = ((((q) * (q))) / 32.2)
    var y: Double = 0.0
    var r: Double = 0.0
    if ((q2g == 0.0)) {
        return 0.0
    }
    when (xsect!!.type) {
        DUMMY -> {
            return 0.0
        }
        RECT_OPEN, RECT_CLOSED -> {
            y = Glibm.pow((q2g / (((xsect!!.wMax) * (xsect!!.wMax)))), (1.0 / 3.0))
        }
        TRIANGULAR -> {
            y = Glibm.pow(((2.0 * q2g) / (((xsect!!.sBot) * (xsect!!.sBot)))), (1.0 / 5.0))
        }
        PARABOLIC -> {
            y = Glibm.pow((((27.0 / 32.0) * q2g) / (((xsect!!.rBot) * (xsect!!.rBot)))), (1.0 / 4.0))
        }
        POWERFUNC -> {
            y = (1.0 / (((2.0 * xsect!!.sBot) + 3.0)))
            y = Glibm.pow(((q2g * ((xsect!!.sBot + 1.0))) / (((xsect!!.rBot) * (xsect!!.rBot)))), y)
        }
        else -> {
            y = (1.01 * Glibm.pow((q2g / xsect!!.yFull), (1.0 / 4.0)))
            if ((y >= xsect!!.yFull)) {
                y = (0.97 * xsect!!.yFull)
            }
            r = (xsect!!.aFull / (((3.141592654 / 4.0) * (((xsect!!.yFull) * (xsect!!.yFull))))))
            if (((r >= 0.5) && (r <= 2.0))) {
                y = getYcritEnum(xsect, q, y)
            } else {
                y = getYcritRidder(xsect, q, y)
            }
        }
    }
    return ((if ((((y) <= (xsect!!.yFull)))) (y) else (xsect!!.yFull)))
}

// xsect.c:1323
fun getTransectParams(xsect: TXsect?, transect: TTransect?) {
    var i: Int = 0
    var iMax: Int = 0
    var wMax: Double = 0.0
    var wTbl: DoubleArray? = transect!!.widthTbl
    xsect!!.yFull = transect!!.yFull
    xsect!!.wMax = transect!!.wMax
    xsect!!.aFull = transect!!.aFull
    xsect!!.rFull = transect!!.rFull
    xsect!!.sFull = (xsect!!.aFull * Glibm.pow(xsect!!.rFull, (2.0 / 3.0)))
    xsect!!.sMax = transect!!.sMax
    xsect!!.aBot = transect!!.aMax
    iMax = 0
    wMax = wTbl!![0]
    i = 1
    f1@ while ((i < transect!!.nTbl)) {
        if ((wTbl!![i] < wMax)) {
            break@f1
        }
        wMax = wTbl!![i]
        iMax = i
        i++
    }
    xsect!!.ywMax = ((xsect!!.yFull * (iMax).toDouble()) / ((((transect!!.nTbl)).toDouble() - 1.0)))
}

// xsect.c:1359
fun generic_getAofS(xsect: TXsect?, s: Double): Double {
    val a = DBox(0.0)
    var a1: Double = 0.0
    var a2: Double = 0.0
    var tol: Double = 0.0
    var xsectStar: TXsectStar? = TXsectStar()
    if ((s <= 0.0)) {
        return 0.0
    }
    if (((((s <= xsect!!.sMax) && (s >= xsect!!.sFull))) && (xsect!!.sMax != xsect!!.sFull))) {
        a1 = xsect!!.aFull
        a2 = xsect_getAmax(xsect)
    } else {
        a1 = 0.0
        a2 = xsect_getAmax(xsect)
    }
    xsectStar!!.xsect = xsect
    xsectStar!!.s = s
    a.v = (0.5 * ((a1 + a2)))
    tol = (0.0001 * xsect!!.aFull)
    findroot_Newton(a1, a2, a, tol, FP4(::evalSofA), xsectStar)
    return a.v
}

// xsect.c:1404
fun evalSofA(a: Double, f: DRef?, df: DRef?, p: Any?) {
    var xsectStar: TXsectStar? = null
    var s: Double = 0.0
    xsectStar = (p as TXsectStar?)
    s = xsect_getSofA(xsectStar!!.xsect, a)
    f!!.set((s - xsectStar!!.s))
    df!!.set(xsect_getdSdA(xsectStar!!.xsect, a))
}

// xsect.c:1424
fun tabular_getdSdA(xsect: TXsect?, a: Double, table: DoubleArray?, nItems: Int): Double {
    var i: Int = 0
    var alpha: Double = (a / xsect!!.aFull)
    var delta: Double = (1.0 / (((nItems).toDouble() - 1.0)))
    var dSdA: Double = 0.0
    i = R.D2I(((alpha / delta)))
    if ((i >= (nItems - 1))) {
        i = (nItems - 2)
    }
    dSdA = (((table!![(i + 1)] - table!![i])) / delta)
    return ((dSdA * xsect!!.sFull) / xsect!!.aFull)
}

// xsect.c:1453
fun generic_getdSdA(xsect: TXsect?, a: Double): Double {
    var a1: Double = 0.0
    var a2: Double = 0.0
    var alpha: Double = (a / xsect!!.aFull)
    var alpha1: Double = (alpha - 0.001)
    var alpha2: Double = (alpha + 0.001)
    if ((alpha1 < 0.0)) {
        alpha1 = 0.0
    }
    a1 = (alpha1 * xsect!!.aFull)
    a2 = (alpha2 * xsect!!.aFull)
    return (((xsect_getSofA(xsect, a2) - xsect_getSofA(xsect, a1))) / ((a2 - a1)))
}

// xsect.c:1474
fun lookup(x: Double, table: DoubleArray?, nItems: Int): Double {
    var delta: Double = 0.0
    var x0: Double = 0.0
    var x1: Double = 0.0
    var y: Double = 0.0
    var y2: Double = 0.0
    var i: Int = 0
    delta = (1.0 / (((nItems).toDouble() - 1.0)))
    i = R.D2I(((x / delta)))
    if ((i >= (nItems - 1))) {
        return table!![(nItems - 1)]
    }
    x0 = ((i).toDouble() * delta)
    x1 = ((((i).toDouble() + 1.0)) * delta)
    y = (table!![i] + ((((x - x0)) * ((table!![(i + 1)] - table!![i]))) / delta))
    if ((i < 2)) {
        y2 = (y + (((((x - x0)) * ((x - x1))) / ((delta * delta))) * ((((table!![i] / 2.0) - table!![(i + 1)]) + (table!![(i + 2)] / 2.0)))))
        if ((y2 > 0.0)) {
            y = y2
        }
    }
    if ((y < 0.0)) {
        y = 0.0
    }
    return y
}

// xsect.c:1511
fun invLookup(y: Double, table: DoubleArray?, nItems: Int): Double {
    var dx: Double = 0.0
    var x: Double = 0.0
    var x0: Double = 0.0
    var dy: Double = 0.0
    var n: Int = 0
    var i: Int = 0
    dx = (1.0 / (((nItems).toDouble() - 1.0)))
    n = nItems
    if ((table!![(n - 3)] > table!![(n - 1)])) {
        n = (n - 2)
    }
    if (((n < nItems) && (y > table!![(nItems - 1)]))) {
        if ((y >= table!![(nItems - 3)])) {
            return ((((n).toDouble() - 1.0)) * dx)
        }
        if ((y <= table!![(nItems - 2)])) {
            i = (nItems - 2)
        } else {
            i = (nItems - 3)
        }
    } else {
        i = locate(y, table, (n - 1))
    }
    if ((i >= (n - 1))) {
        return ((((n).toDouble() - 1.0)) * dx)
    }
    x0 = ((i).toDouble() * dx)
    dy = (table!![(i + 1)] - table!![i])
    if ((dy == 0.0)) {
        x = x0
    } else {
        x = (x0 + ((((y - table!![i])) * dx) / dy))
    }
    if ((x < 0.0)) {
        x = 0.0
    }
    if ((x > 1.0)) {
        x = 1.0
    }
    return x
}

// xsect.c:1571
fun locate(y: Double, table: DoubleArray?, jLast: Int): Int {
    var j: Int = 0
    var j1: Int = 0
    var j2: Int = jLast
    if ((y <= table!![0])) {
        return 0
    }
    if ((y >= table!![jLast])) {
        return jLast
    }
    w1@ while (((j2 - j1) > 1)) {
        j = (((j1 + j2)) shr 1)
        if ((y >= table!![j])) {
            j1 = j
        } else {
            j2 = j
        }
    }
    return j1
}

// xsect.c:1612
fun getQcritical(yc: Double, p: Any?): Double {
    var a: Double = 0.0
    var w: Double = 0.0
    var qc: Double = 0.0
    var xsectStar: TXsectStar? = null
    xsectStar = (p as TXsectStar?)
    a = xsect_getAofY(xsectStar!!.xsect, yc)
    w = xsect_getWofY(xsectStar!!.xsect, yc)
    qc = (-xsectStar!!.qc)
    if ((w > 0.0)) {
        qc = ((a * sqrt(((32.2 * a) / w))) - xsectStar!!.qc)
    }
    return qc
}

// xsect.c:1634
fun getYcritEnum(xsect: TXsect?, q: Double, y0: Double): Double {
    var q0: Double = 0.0
    var dy: Double = 0.0
    var qc: Double = 0.0
    var yc: Double = 0.0
    var i1: Int = 0
    var i: Int = 0
    var xsectStar: TXsectStar? = TXsectStar()
    dy = (xsect!!.yFull / 25.0)
    i1 = R.D2I(((y0 / dy)))
    xsectStar!!.xsect = xsect
    xsectStar!!.qc = 0.0
    q0 = getQcritical(((i1).toDouble() * dy), xsectStar)
    if ((q0 < q)) {
        yc = xsect!!.yFull
        i = (i1 + 1)
        f1@ while ((i <= 25)) {
            qc = getQcritical(((i).toDouble() * dy), xsectStar)
            if ((qc >= q)) {
                yc = ((((((q - q0)) / ((qc - q0))) + (((i).toDouble() - 1.0)))) * dy)
                break@f1
            }
            q0 = qc
            i++
        }
    } else {
        yc = 0.0
        i = (i1 - 1)
        f2@ while ((i >= 0)) {
            qc = getQcritical(((i).toDouble() * dy), xsectStar)
            if ((qc < q)) {
                yc = ((((((q - qc)) / ((q0 - qc))) + (i).toDouble())) * dy)
                break@f2
            }
            q0 = qc
            i--
        }
    }
    return yc
}

// xsect.c:1700
fun getYcritRidder(xsect: TXsect?, q: Double, y0: Double): Double {
    var y1: Double = 0.0
    var y2: Double = (0.99 * xsect!!.yFull)
    var yc: Double = 0.0
    var q0: Double = 0.0
    var q1: Double = 0.0
    var q2: Double = 0.0
    var xsectStar: TXsectStar? = TXsectStar()
    xsectStar!!.xsect = xsect
    xsectStar!!.qc = 0.0
    q2 = getQcritical(y2, xsectStar)
    if ((q2 < q)) {
        return xsect!!.yFull
    }
    q0 = getQcritical(y0, xsectStar)
    q1 = getQcritical((0.5 * xsect!!.yFull), xsectStar)
    if ((q0 > q)) {
        y2 = y0
        if ((q1 < q)) {
            y1 = (0.5 * xsect!!.yFull)
        }
    } else {
        y1 = y0
        if ((q1 > q)) {
            y2 = (0.5 * xsect!!.yFull)
        }
    }
    xsectStar!!.qc = q
    yc = findroot_Ridder(y1, y2, 0.001, FP3(::getQcritical), xsectStar)
    return yc
}

// xsect.c:1755
fun rect_closed_getSofA(xsect: TXsect?, a: Double): Double {
    var alfMax: Double = 0.97
    if (((a / xsect!!.aFull) > alfMax)) {
        return (xsect!!.sMax + ((((xsect!!.sFull - xsect!!.sMax)) * (((a / xsect!!.aFull) - alfMax))) / ((1.0 - alfMax))))
    }
    return (a * Glibm.pow(xsect_getRofA(xsect, a), (2.0 / 3.0)))
}

// xsect.c:1770
fun rect_closed_getdSdA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = 0.0
    var alfMax: Double = 0.0
    var r: Double = 0.0
    alfMax = 0.97
    alpha = (a / xsect!!.aFull)
    if ((alpha > alfMax)) {
        return (((xsect!!.sFull - xsect!!.sMax)) / ((((1.0 - alfMax)) * xsect!!.aFull)))
    }
    if ((alpha <= 1.0e-30)) {
        return generic_getdSdA(xsect, a)
    }
    r = xsect_getRofA(xsect, a)
    return ((((5.0 / 3.0) - ((((2.0 / 3.0)) * ((2.0 / xsect!!.wMax))) * r))) * Glibm.pow(r, (2.0 / 3.0)))
}

// xsect.c:1793
fun rect_closed_getRofA(xsect: TXsect?, a: Double): Double {
    var p: Double = 0.0
    if ((a <= 0.0)) {
        return 0.0
    }
    p = (xsect!!.wMax + ((2.0 * a) / xsect!!.wMax))
    if (((a / xsect!!.aFull) > 0.97)) {
        p += (((((a / xsect!!.aFull) - 0.97)) / ((1.0 - 0.97))) * xsect!!.wMax)
    }
    return (a / p)
}

// xsect.c:1810
fun rect_open_getSofA(xsect: TXsect?, a: Double): Double {
    var y: Double = (a / xsect!!.wMax)
    var r: Double = (a / (((((2.0 - xsect!!.sBot)) * y) + xsect!!.wMax)))
    return (a * Glibm.pow(r, (2.0 / 3.0)))
}

// xsect.c:1818
fun rect_open_getdSdA(xsect: TXsect?, a: Double): Double {
    var r: Double = 0.0
    var dPdA: Double = 0.0
    if (((a / xsect!!.aFull) <= 1.0e-30)) {
        return generic_getdSdA(xsect, a)
    }
    r = xsect_getRofA(xsect, a)
    dPdA = (((2.0 - xsect!!.sBot)) / xsect!!.wMax)
    return ((((5.0 / 3.0) - ((((2.0 / 3.0)) * dPdA) * r))) * Glibm.pow(r, (2.0 / 3.0)))
}

// xsect.c:1837
fun rect_triang_getYofA(xsect: TXsect?, a: Double): Double {
    if ((a <= xsect!!.aBot)) {
        return sqrt((a / xsect!!.sBot))
    } else {
        return (xsect!!.yBot + (((a - xsect!!.aBot)) / xsect!!.wMax))
    }
    throw IllegalStateException("rect_triang_getYofA: no return")
}

// xsect.c:1846
fun rect_triang_getRofA(xsect: TXsect?, a: Double): Double {
    var y: Double = 0.0
    var p: Double = 0.0
    var alf: Double = 0.0
    if ((a <= 0.0)) {
        return 0.0
    }
    y = rect_triang_getYofA(xsect, a)
    if ((y <= xsect!!.yBot)) {
        return (a / (((2.0 * y) * xsect!!.rBot)))
    }
    p = (((2.0 * xsect!!.yBot) * xsect!!.rBot) + (2.0 * ((y - xsect!!.yBot))))
    alf = (((a / xsect!!.aFull)) - 0.98)
    if ((alf > 0.0)) {
        p += ((alf / ((1.0 - 0.98))) * xsect!!.wMax)
    }
    return (a / p)
}

// xsect.c:1866
fun rect_triang_getSofA(xsect: TXsect?, a: Double): Double {
    var alfMax: Double = 0.98
    if (((a / xsect!!.aFull) > alfMax)) {
        return (xsect!!.sMax + ((((xsect!!.sFull - xsect!!.sMax)) * (((a / xsect!!.aFull) - alfMax))) / ((1.0 - alfMax))))
    } else {
        return (a * Glibm.pow(rect_triang_getRofA(xsect, a), (2.0 / 3.0)))
    }
    throw IllegalStateException("rect_triang_getSofA: no return")
}

// xsect.c:1879
fun rect_triang_getdSdA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = 0.0
    var alfMax: Double = 0.0
    var dPdA: Double = 0.0
    var r: Double = 0.0
    alfMax = 0.98
    alpha = (a / xsect!!.aFull)
    if ((alpha > alfMax)) {
        return (((xsect!!.sFull - xsect!!.sMax)) / ((((1.0 - alfMax)) * xsect!!.aFull)))
    }
    if ((alpha <= 1.0e-30)) {
        return generic_getdSdA(xsect, a)
    }
    if ((a > xsect!!.aBot)) {
        dPdA = (2.0 / xsect!!.wMax)
    } else {
        dPdA = (xsect!!.rBot / sqrt((a * xsect!!.sBot)))
    }
    r = rect_triang_getRofA(xsect, a)
    return ((((5.0 / 3.0) - ((((2.0 / 3.0)) * dPdA) * r))) * Glibm.pow(r, (2.0 / 3.0)))
}

// xsect.c:1902
fun rect_triang_getAofY(xsect: TXsect?, y: Double): Double {
    if ((y <= xsect!!.yBot)) {
        return ((y * y) * xsect!!.sBot)
    } else {
        return (xsect!!.aBot + (((y - xsect!!.yBot)) * xsect!!.wMax))
    }
    throw IllegalStateException("rect_triang_getAofY: no return")
}

// xsect.c:1908
fun rect_triang_getRofY(xsect: TXsect?, y: Double): Double {
    var p: Double = 0.0
    var a: Double = 0.0
    var alf: Double = 0.0
    if ((y <= xsect!!.yBot)) {
        return ((y * xsect!!.sBot) / ((2.0 * xsect!!.rBot)))
    }
    a = (xsect!!.aBot + (((y - xsect!!.yBot)) * xsect!!.wMax))
    p = (((2.0 * xsect!!.yBot) * xsect!!.rBot) + (2.0 * ((y - xsect!!.yBot))))
    alf = (((a / xsect!!.aFull)) - 0.98)
    if ((alf > 0.0)) {
        p += ((alf / ((1.0 - 0.98))) * xsect!!.wMax)
    }
    return (a / p)
}

// xsect.c:1927
fun rect_triang_getWofY(xsect: TXsect?, y: Double): Double {
    if ((y <= xsect!!.yBot)) {
        return ((2.0 * xsect!!.sBot) * y)
    } else {
        return xsect!!.wMax
    }
    throw IllegalStateException("rect_triang_getWofY: no return")
}

// xsect.c:1938
fun rect_round_getYofA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = 0.0
    if ((a > xsect!!.aBot)) {
        return (xsect!!.yBot + (((a - xsect!!.aBot)) / xsect!!.wMax))
    }
    alpha = (a / (((3.141592654 * xsect!!.rBot) * xsect!!.rBot)))
    if ((alpha < 0.04)) {
        return (((2.0 * xsect!!.rBot)) * getYcircular(alpha))
    }
    return (((2.0 * xsect!!.rBot)) * lookup(alpha, Y_Circ, N_Y_Circ))
}

// xsect.c:1952
fun rect_round_getRofA(xsect: TXsect?, a: Double): Double {
    var y1: Double = 0.0
    var theta1: Double = 0.0
    var p: Double = 0.0
    var arg: Double = 0.0
    if ((a <= 0.0)) {
        return 0.0
    }
    if ((a > xsect!!.aBot)) {
        y1 = (((a - xsect!!.aBot)) / xsect!!.wMax)
        theta1 = (2.0 * Glibm.asin(((xsect!!.wMax / 2.0) / xsect!!.rBot)))
        p = ((xsect!!.rBot * theta1) + (2.0 * y1))
        arg = (((a / xsect!!.aFull)) - 0.98)
        if ((arg > 0.0)) {
            p += ((arg / ((1.0 - 0.98))) * xsect!!.wMax)
        }
        return (a / p)
    }
    y1 = rect_round_getYofA(xsect, a)
    theta1 = (2.0 * Glibm.acos((1.0 - (y1 / xsect!!.rBot))))
    p = (xsect!!.rBot * theta1)
    return (a / p)
}

// xsect.c:1978
fun rect_round_getSofA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = 0.0
    var aFull: Double = 0.0
    var sFull: Double = 0.0
    var alfMax: Double = 0.98
    if (((a / xsect!!.aFull) > alfMax)) {
        return (xsect!!.sMax + ((((xsect!!.sFull - xsect!!.sMax)) * (((a / xsect!!.aFull) - alfMax))) / ((1.0 - alfMax))))
    } else if ((a > xsect!!.aBot)) {
        return (a * Glibm.pow(xsect_getRofA(xsect, a), (2.0 / 3.0)))
    } else {
        aFull = ((3.141592654 * xsect!!.rBot) * xsect!!.rBot)
        alpha = (a / aFull)
        sFull = xsect!!.sBot
        if ((alpha < 0.04)) {
            return (sFull * getScircular(alpha))
        } else {
            return (sFull * lookup(alpha, S_Circ, N_S_Circ))
        }
    }
    throw IllegalStateException("rect_round_getSofA: no return")
}

// xsect.c:2013
fun rect_round_getdSdA(xsect: TXsect?, a: Double): Double {
    var alfMax: Double = 0.0
    var r: Double = 0.0
    var dPdA: Double = 0.0
    alfMax = 0.98
    if (((a / xsect!!.aFull) > alfMax)) {
        return (((xsect!!.sFull - xsect!!.sMax)) / ((((1.0 - alfMax)) * xsect!!.aFull)))
    } else if ((a > xsect!!.aBot)) {
        r = rect_round_getRofA(xsect, a)
        dPdA = (2.0 / xsect!!.wMax)
        return ((((5.0 / 3.0) - ((((2.0 / 3.0)) * dPdA) * r))) * Glibm.pow(r, (2.0 / 3.0)))
    } else {
        return generic_getdSdA(xsect, a)
    }
    throw IllegalStateException("rect_round_getdSdA: no return")
}

// xsect.c:2038
fun rect_round_getAofY(xsect: TXsect?, y: Double): Double {
    var theta1: Double = 0.0
    if ((y > xsect!!.yBot)) {
        return (xsect!!.aBot + (((y - xsect!!.yBot)) * xsect!!.wMax))
    }
    theta1 = (2.0 * Glibm.acos((1.0 - (y / xsect!!.rBot))))
    return (((0.5 * xsect!!.rBot) * xsect!!.rBot) * ((theta1 - Glibm.sin(theta1))))
}

// xsect.c:2051
fun rect_round_getRofY(xsect: TXsect?, y: Double): Double {
    var theta1: Double = 0.0
    if ((y <= 0.0)) {
        return 0.0
    }
    if ((y > xsect!!.yBot)) {
        return rect_round_getRofA(xsect, rect_round_getAofY(xsect, y))
    }
    theta1 = (2.0 * Glibm.acos((1.0 - (y / xsect!!.rBot))))
    return (((0.5 * xsect!!.rBot) * ((1.0 - Glibm.sin(theta1)))) / theta1)
}

// xsect.c:2065
fun rect_round_getWofY(xsect: TXsect?, y: Double): Double {
    if ((y > xsect!!.yBot)) {
        return xsect!!.wMax
    }
    return (2.0 * sqrt((y * (((2.0 * xsect!!.rBot) - y)))))
}

// xsect.c:2082
fun mod_basket_getYofA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = 0.0
    var y1: Double = 0.0
    if ((a <= (xsect!!.aFull - xsect!!.aBot))) {
        return (a / xsect!!.wMax)
    }
    alpha = (((xsect!!.aFull - a)) / (((3.141592654 * xsect!!.rBot) * xsect!!.rBot)))
    if ((alpha < 0.04)) {
        y1 = getYcircular(alpha)
    } else {
        y1 = lookup(alpha, Y_Circ, N_Y_Circ)
    }
    y1 = ((2.0 * xsect!!.rBot) * y1)
    return (xsect!!.yFull - y1)
}

// xsect.c:2101
fun mod_basket_getRofA(xsect: TXsect?, a: Double): Double {
    var y1: Double = 0.0
    var p: Double = 0.0
    var theta1: Double = 0.0
    if ((a <= (xsect!!.aFull - xsect!!.aBot))) {
        return (a / ((xsect!!.wMax + ((2.0 * a) / xsect!!.wMax))))
    }
    y1 = (xsect!!.yFull - mod_basket_getYofA(xsect, a))
    theta1 = (2.0 * Glibm.acos((1.0 - (y1 / xsect!!.rBot))))
    p = (((xsect!!.sBot - theta1)) * xsect!!.rBot)
    y1 = (xsect!!.yFull - xsect!!.yBot)
    p = ((p + (2.0 * y1)) + xsect!!.wMax)
    return (a / p)
}

// xsect.c:2128
fun mod_basket_getdSdA(xsect: TXsect?, a: Double): Double {
    var r: Double = 0.0
    var dPdA: Double = 0.0
    if (((a <= (xsect!!.aFull - xsect!!.aBot)) && ((a / xsect!!.aFull) > 1.0e-30))) {
        r = (a / ((xsect!!.wMax + ((2.0 * a) / xsect!!.wMax))))
        dPdA = (2.0 / xsect!!.wMax)
        return ((((5.0 / 3.0) - ((((2.0 / 3.0)) * dPdA) * r))) * Glibm.pow(r, (2.0 / 3.0)))
    } else {
        return generic_getdSdA(xsect, a)
    }
    throw IllegalStateException("mod_basket_getdSdA: no return")
}

// xsect.c:2145
fun mod_basket_getAofY(xsect: TXsect?, y: Double): Double {
    var a1: Double = 0.0
    var theta1: Double = 0.0
    var y1: Double = 0.0
    if ((y <= (xsect!!.yFull - xsect!!.yBot))) {
        return (y * xsect!!.wMax)
    }
    y1 = (xsect!!.yFull - y)
    theta1 = (2.0 * Glibm.acos((1.0 - (y1 / xsect!!.rBot))))
    a1 = (((0.5 * xsect!!.rBot) * xsect!!.rBot) * ((theta1 - Glibm.sin(theta1))))
    return (xsect!!.aFull - a1)
}

// xsect.c:2162
fun mod_basket_getWofY(xsect: TXsect?, y: Double): Double {
    var y1: Double = 0.0
    if ((y <= 0.0)) {
        return 0.0
    }
    if ((y <= (xsect!!.yFull - xsect!!.yBot))) {
        return xsect!!.wMax
    }
    y1 = (xsect!!.yFull - y)
    return (2.0 * sqrt((y1 * (((2.0 * xsect!!.rBot) - y1)))))
}

// xsect.c:2184
fun trapez_getYofA(xsect: TXsect?, a: Double): Double {
    if ((xsect!!.sBot == 0.0)) {
        return (a / xsect!!.yBot)
    }
    return (((sqrt(((xsect!!.yBot * xsect!!.yBot) + ((4.0 * xsect!!.sBot) * a))) - xsect!!.yBot)) / ((2.0 * xsect!!.sBot)))
}

// xsect.c:2191
fun trapez_getRofA(xsect: TXsect?, a: Double): Double {
    return (a / ((xsect!!.yBot + (trapez_getYofA(xsect, a) * xsect!!.rBot))))
}

// xsect.c:2196
fun trapez_getdSdA(xsect: TXsect?, a: Double): Double {
    var r: Double = 0.0
    var dPdA: Double = 0.0
    if (((a / xsect!!.aFull) <= 1.0e-30)) {
        return generic_getdSdA(xsect, a)
    }
    r = trapez_getRofA(xsect, a)
    dPdA = (xsect!!.rBot / sqrt(((xsect!!.yBot * xsect!!.yBot) + ((4.0 * xsect!!.sBot) * a))))
    return ((((5.0 / 3.0) - ((((2.0 / 3.0)) * dPdA) * r))) * Glibm.pow(r, (2.0 / 3.0)))
}

// xsect.c:2210
fun trapez_getAofY(xsect: TXsect?, y: Double): Double {
    return (((xsect!!.yBot + (xsect!!.sBot * y))) * y)
}

// xsect.c:2215
fun trapez_getRofY(xsect: TXsect?, y: Double): Double {
    if ((y == 0.0)) {
        return 0.0
    }
    return (trapez_getAofY(xsect, y) / ((xsect!!.yBot + (y * xsect!!.rBot))))
}

// xsect.c:2221
fun trapez_getWofY(xsect: TXsect?, y: Double): Double {
    return (xsect!!.yBot + ((2.0 * y) * xsect!!.sBot))
}

// xsect.c:2231
fun triang_getYofA(xsect: TXsect?, a: Double): Double {
    return sqrt((a / xsect!!.sBot))
}

// xsect.c:2236
fun triang_getRofA(xsect: TXsect?, a: Double): Double {
    return (a / (((2.0 * triang_getYofA(xsect, a)) * xsect!!.rBot)))
}

// xsect.c:2241
fun triang_getdSdA(xsect: TXsect?, a: Double): Double {
    var r: Double = 0.0
    var dPdA: Double = 0.0
    if (((a / xsect!!.aFull) <= 1.0e-30)) {
        return generic_getdSdA(xsect, a)
    }
    r = triang_getRofA(xsect, a)
    dPdA = (xsect!!.rBot / sqrt((a * xsect!!.sBot)))
    return ((((5.0 / 3.0) - ((((2.0 / 3.0)) * dPdA) * r))) * Glibm.pow(r, (2.0 / 3.0)))
}

// xsect.c:2253
fun triang_getAofY(xsect: TXsect?, y: Double): Double {
    return ((y * y) * xsect!!.sBot)
}

// xsect.c:2258
fun triang_getRofY(xsect: TXsect?, y: Double): Double {
    return (((y * xsect!!.sBot)) / ((2.0 * xsect!!.rBot)))
}

// xsect.c:2263
fun triang_getWofY(xsect: TXsect?, y: Double): Double {
    return ((2.0 * xsect!!.sBot) * y)
}

// xsect.c:2273
fun parab_getYofA(xsect: TXsect?, a: Double): Double {
    return Glibm.pow(((((3.0 / 4.0)) * a) / xsect!!.rBot), (2.0 / 3.0))
}

// xsect.c:2278
fun parab_getRofA(xsect: TXsect?, a: Double): Double {
    if ((a <= 0.0)) {
        return 0.0
    }
    return (a / parab_getPofY(xsect, parab_getYofA(xsect, a)))
}

// xsect.c:2284
fun parab_getPofY(xsect: TXsect?, y: Double): Double {
    var x: Double = ((2.0 * sqrt(y)) / xsect!!.rBot)
    var t: Double = sqrt((1.0 + (x * x)))
    return (((0.5 * xsect!!.rBot) * xsect!!.rBot) * (((x * t) + Glibm.log((x + t)))))
}

// xsect.c:2291
fun parab_getAofY(xsect: TXsect?, y: Double): Double {
    return (((((4.0 / 3.0) * xsect!!.rBot) * y) * sqrt(y)))
}

// xsect.c:2296
fun parab_getRofY(xsect: TXsect?, y: Double): Double {
    if ((y <= 0.0)) {
        return 0.0
    }
    return (parab_getAofY(xsect, y) / parab_getPofY(xsect, y))
}

// xsect.c:2302
fun parab_getWofY(xsect: TXsect?, y: Double): Double {
    return ((2.0 * xsect!!.rBot) * sqrt(y))
}

// xsect.c:2312
fun powerfunc_getYofA(xsect: TXsect?, a: Double): Double {
    return Glibm.pow((a / xsect!!.rBot), (1.0 / ((xsect!!.sBot + 1.0))))
}

// xsect.c:2317
fun powerfunc_getRofA(xsect: TXsect?, a: Double): Double {
    if ((a <= 0.0)) {
        return 0.0
    }
    return (a / powerfunc_getPofY(xsect, powerfunc_getYofA(xsect, a)))
}

// xsect.c:2323
fun powerfunc_getPofY(xsect: TXsect?, y: Double): Double {
    var dy1: Double = (0.02 * xsect!!.yFull)
    var h: Double = ((((xsect!!.sBot + 1.0)) * xsect!!.rBot) / 2.0)
    var m: Double = xsect!!.sBot
    var p: Double = 0.0
    var y1: Double = 0.0
    var x1: Double = 0.0
    var x2: Double = 0.0
    var y2: Double = 0.0
    var dx: Double = 0.0
    var dy: Double = 0.0
    d1@ do {
        y2 = (y1 + dy1)
        if ((y2 > y)) {
            y2 = y
        }
        x2 = (h * Glibm.pow(y2, m))
        dx = (x2 - x1)
        dy = (y2 - y1)
        p += (sqrt(((dx * dx) + (dy * dy))))
        x1 = x2
        y1 = y2
    } while ((y2 < y))
    return (2.0 * p)
}

// xsect.c:2346
fun powerfunc_getAofY(xsect: TXsect?, y: Double): Double {
    return (xsect!!.rBot * Glibm.pow(y, (xsect!!.sBot + 1.0)))
}

// xsect.c:2351
fun powerfunc_getRofY(xsect: TXsect?, y: Double): Double {
    if ((y <= 0.0)) {
        return 0.0
    }
    return (powerfunc_getAofY(xsect, y) / powerfunc_getPofY(xsect, y))
}

// xsect.c:2357
fun powerfunc_getWofY(xsect: TXsect?, y: Double): Double {
    return ((((xsect!!.sBot + 1.0)) * xsect!!.rBot) * Glibm.pow(y, xsect!!.sBot))
}

// xsect.c:2367
fun circ_getYofA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = (a / xsect!!.aFull)
    if ((alpha < 0.04)) {
        return (xsect!!.yFull * getYcircular(alpha))
    } else {
        return (xsect!!.yFull * lookup(alpha, Y_Circ, N_Y_Circ))
    }
    throw IllegalStateException("circ_getYofA: no return")
}

// xsect.c:2378
fun circ_getAofS(xsect: TXsect?, s: Double): Double {
    var psi: Double = (s / xsect!!.sFull)
    if ((psi == 0.0)) {
        return 0.0
    }
    if ((psi >= 1.0)) {
        return xsect!!.aFull
    }
    if ((psi <= 0.015)) {
        return (xsect!!.aFull * getAcircular(psi))
    } else {
        return (xsect!!.aFull * invLookup(psi, S_Circ, N_S_Circ))
    }
    throw IllegalStateException("circ_getAofS: no return")
}

// xsect.c:2391
fun circ_getSofA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = (a / xsect!!.aFull)
    if ((alpha < 0.04)) {
        return (xsect!!.sFull * getScircular(alpha))
    } else {
        return (xsect!!.sFull * lookup(alpha, S_Circ, N_S_Circ))
    }
    throw IllegalStateException("circ_getSofA: no return")
}

// xsect.c:2403
fun circ_getdSdA(xsect: TXsect?, a: Double): Double {
    var alpha: Double = 0.0
    var theta: Double = 0.0
    var p: Double = 0.0
    var r: Double = 0.0
    var dPdA: Double = 0.0
    alpha = (a / xsect!!.aFull)
    if ((alpha <= 1.0e-30)) {
        return 1.0e-30
    } else if ((alpha < 0.04)) {
        theta = getThetaOfAlpha(alpha)
        p = ((theta * xsect!!.yFull) / 2.0)
        r = (a / p)
        dPdA = ((4.0 / xsect!!.yFull) / ((1.0 - Glibm.cos(theta))))
        return ((((5.0 / 3.0) - ((((2.0 / 3.0)) * dPdA) * r))) * Glibm.pow(r, (2.0 / 3.0)))
    } else {
        return tabular_getdSdA(xsect, a, S_Circ, N_S_Circ)
    }
    throw IllegalStateException("circ_getdSdA: no return")
}

// xsect.c:2450
fun circ_getAofY(xsect: TXsect?, y: Double): Double {
    var yNorm: Double = 0.0
    yNorm = (y / xsect!!.yFull)
    return (xsect!!.aFull * lookup(yNorm, A_Circ, N_A_Circ))
}

// xsect.c:2462
fun filled_circ_getYofA(xsect: TXsect?, __a: Double): Double {
    var a: Double = __a
    var y: Double = 0.0
    xsect!!.yFull += xsect!!.yBot
    xsect!!.aFull += xsect!!.aBot
    a += xsect!!.aBot
    y = circ_getYofA(xsect, a)
    y -= xsect!!.yBot
    xsect!!.yFull -= xsect!!.yBot
    xsect!!.aFull -= xsect!!.aBot
    return y
}

// xsect.c:2481
fun filled_circ_getAofY(xsect: TXsect?, __y: Double): Double {
    var y: Double = __y
    var a: Double = 0.0
    xsect!!.yFull += xsect!!.yBot
    xsect!!.aFull += xsect!!.aBot
    y += xsect!!.yBot
    a = circ_getAofY(xsect, y)
    a -= xsect!!.aBot
    xsect!!.yFull -= xsect!!.yBot
    xsect!!.aFull -= xsect!!.aBot
    return a
}

// xsect.c:2500
fun filled_circ_getRofY(xsect: TXsect?, __y: Double): Double {
    var y: Double = __y
    var a: Double = 0.0
    var r: Double = 0.0
    var p: Double = 0.0
    xsect!!.yFull += xsect!!.yBot
    xsect!!.aFull += xsect!!.aBot
    y += xsect!!.yBot
    a = circ_getAofY(xsect, y)
    r = ((0.25 * xsect!!.yFull) * lookup((y / xsect!!.yFull), R_Circ, N_R_Circ))
    p = ((a / r))
    a = (a - xsect!!.aBot)
    p = ((p - xsect!!.rBot) + xsect!!.sBot)
    r = (a / p)
    xsect!!.yFull -= xsect!!.yBot
    xsect!!.aFull -= xsect!!.aBot
    return r
}

// xsect.c:2531
fun getYcircular(alpha: Double): Double {
    var theta: Double = 0.0
    if ((alpha >= 1.0)) {
        return 1.0
    }
    if ((alpha <= 0.0)) {
        return 0.0
    }
    if ((alpha <= 1.0e-5)) {
        theta = Glibm.pow((37.6911 * alpha), (1.0 / 3.0))
        return ((theta * theta) / 16.0)
    }
    theta = getThetaOfAlpha(alpha)
    return (((1.0 - Glibm.cos((theta / 2.0)))) / 2.0)
}

// xsect.c:2545
fun getScircular(alpha: Double): Double {
    var theta: Double = 0.0
    if ((alpha >= 1.0)) {
        return 1.0
    }
    if ((alpha <= 0.0)) {
        return 0.0
    }
    if ((alpha <= 1.0e-5)) {
        theta = Glibm.pow((37.6911 * alpha), (1.0 / 3.0))
        return (Glibm.pow(theta, (13.0 / 3.0)) / 124.4797)
    }
    theta = getThetaOfAlpha(alpha)
    return ((Glibm.pow(((theta - Glibm.sin(theta))), (5.0 / 3.0)) / ((2.0 * 3.141592654))) / Glibm.pow(theta, (2.0 / 3.0)))
}

// xsect.c:2559
fun getAcircular(psi: Double): Double {
    var theta: Double = 0.0
    if ((psi >= 1.0)) {
        return 1.0
    }
    if ((psi <= 0.0)) {
        return 0.0
    }
    if ((psi <= 1.0e-6)) {
        theta = Glibm.pow((124.4797 * psi), (3.0 / 13.0))
        return (((theta * theta) * theta) / 37.6911)
    }
    theta = getThetaOfPsi(psi)
    return (((theta - Glibm.sin(theta))) / ((2.0 * 3.141592654)))
}

// xsect.c:2573
fun getThetaOfAlpha(alpha: Double): Double {
    var k: Int = 0
    var theta: Double = 0.0
    var theta1: Double = 0.0
    var ap: Double = 0.0
    var d: Double = 0.0
    if ((alpha > 0.04)) {
        theta = (1.2 + ((5.08 * ((alpha - 0.04))) / 0.96))
    } else {
        theta = ((0.031715 - (12.79384 * alpha)) + (8.28479 * sqrt(alpha)))
    }
    theta1 = theta
    ap = (((2.0 * 3.141592654)) * alpha)
    k = 1
    f1@ while ((k <= 40)) {
        d = ((-(((ap - theta) + Glibm.sin(theta)))) / ((1.0 - Glibm.cos(theta))))
        if ((d > 1.0)) {
            d = ((if (((d) >= 0.0)) abs(1.0) else (-(abs(1.0)))))
        }
        theta = (theta - d)
        if ((abs(d) <= 0.0001)) {
            return theta
        }
        k++
    }
    return theta1
}

// xsect.c:2593
fun getThetaOfPsi(psi: Double): Double {
    var k: Int = 0
    var theta: Double = 0.0
    var theta1: Double = 0.0
    var ap: Double = 0.0
    var tt: Double = 0.0
    var tt23: Double = 0.0
    var t3: Double = 0.0
    var d: Double = 0.0
    if ((psi > 0.90)) {
        theta = (4.17 + ((1.12 * ((psi - 0.90))) / 0.176))
    } else if ((psi > 0.5)) {
        theta = (3.14 + ((1.03 * ((psi - 0.5))) / 0.4))
    } else if ((psi > 0.015)) {
        theta = (1.2 + ((1.94 * ((psi - 0.015))) / 0.485))
    } else {
        theta = ((0.12103 - (55.5075 * psi)) + (15.62254 * sqrt(psi)))
    }
    theta1 = theta
    ap = (((2.0 * 3.141592654)) * psi)
    k = 1
    f1@ while ((k <= 40)) {
        theta = abs(theta)
        tt = (theta - Glibm.sin(theta))
        tt23 = Glibm.pow(tt, (2.0 / 3.0))
        t3 = Glibm.pow(theta, (1.0 / 3.0))
        d = (((ap * theta) / t3) - (tt * tt23))
        d = (d / ((((ap * ((2.0 / 3.0))) / t3) - ((((5.0 / 3.0)) * tt23) * ((1.0 - Glibm.cos(theta)))))))
        theta = (theta - d)
        if ((abs(d) <= 0.0001)) {
            return theta
        }
        k++
    }
    return theta1
}

