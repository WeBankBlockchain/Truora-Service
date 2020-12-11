

### VRF(基于K1曲线生成)

#### 简介

- **什么是VRF**：持有私钥 `sk` 的 `prover` 可以计算哈希，持有公钥 `pk` 的 `verifier` 可以验证哈希的正确性

- **VRF v.s. 签名算法**：签名算法不具有唯一性，即同样的私钥、公钥生成的签名不一定相同

- **VRF v.s. 哈希函数**：哈希函数不具有可验证性

  

#### VRF详细介绍

##### 相关术语

- SK：VRF私钥；
- PK：VRF公钥；
- alpha: VRF输入
- beta：VRF输出
- pi：VRF证明
- Prover：同时持有SK和PK，可生成VRF随机数和证明；
- Verifier：持有公钥，可验证VRF随机性



##### VRF算法主要流程

- **VRF哈希**：相同的输入和SK，输出相同

  `beta = VRF_hash(SK, alpha)`

- **VRF证明生成：**

  `pi = VRF_prove(SK, alpha)`

- **VRF证明转哈希：**

  `beta = VRF_proof2Hash(pi)`

  即：

  `VRF_hash(SK, alpha) == VRF_proof2Hash(pi)`

- **VRF随机数验证：**

  `VRF_verify(PK, alpha, pi)`



##### VRF安全特性

- **完全唯一性(Full Uniqueness)**：给定VRF私钥和输入，有且仅有一个VRF输出随机数可被验证有效；给定VRF私钥SK和输入alpha，攻击者不可产生不同的`{beta1, pi1}`和`{beta2, pi2}`，满足`VRF_verify(PK, alpha, pi1)`和`VRF_verify(PK, alpha, pi2)`均验证有效；
- **完全抗冲突性(Full collison Resistance)**：即使在攻击者攻破VRF密钥`SK`的情况下，攻击者也无法找到两个不同的输入`alpha1`和`alpha2`，使得`VRF_hash(SK, alpha1) == VRF_hash(SK, alpha2)`
- **完全伪随机性(Full Pseudorandomness)**: 攻击者不持有VRF proof `pi`的情况下，无法将VRF随机数与通常的随机数区分开；

#### 基于椭圆曲线的VRF实现(EC-VRF)

##### 安全特性

- 可信唯一性

- 可信抗冲突性

- 完全伪随机性

  

##### 相关符号说明

- `F`：有限域
- `2n`: 有限域长度
- `E`:  定义在有限域F上的椭圆曲线
- `m`: EC point的长度
- `G`：E在大素数阶上的子群
- `q`：群G的大素数阶
- `g`: 群G的生成元
- `Hash`：哈希函数
- `hLen`: 哈希的长度

##### 公私钥

- **公钥**：` sk*G
- **私钥**：随机数`sk`，在`(0, q)`范围内



##### VRF证明生成：ECVRF_prove(y, x, alpha)

- 输入：公钥`pk`；私钥`sk`；输入`alpha`;

- 输出：VRF证明(长度是m+3*n)

- 步骤：

  (1) 调用`ECVRF_hash_to_curve(pk, alpha)`，将输入alpha转换成椭圆曲线`h`；

  (2) 计算`gamma = h*sk`

  (3) 选取`[0, q - 1]`的随机数`k`

  (4) 调用`ECVRF_hash_points`计算`c=Hash(g, h, y, gamma, g*k, h*k)`，记为`c`

  (5) `s = k - c * sk`

  (6) `pi = {gamma, c, s}`

  

##### VRF随机数验证：ECVRF_verify(y, pi, alpha)

- 输入：公钥y；VRF证明pi; VRF输入alpha

- 输出：`valid` or `invaid`

- 步骤：

  (1) 解码VRF proof:  `{gamma, c, s} = ECVRF_decode_proof(pi)`

  (2) `u = pk*c + g*s`

  (3) `h = ECVRF_hash_to_curve(pk, alpha)`

  (4)  `v = gamma*c + h*s`

  (5)  `c'= Hash(g, h, y, gamma, u, v)`

  (6) `c == c', valid; 否则，invalid`

  
