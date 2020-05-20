import JSEncrypt from 'jsencrypt/bin/jsencrypt'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChBaGiw4ugvRg+v5BtP2jrE0DOjmNH0LbATy5W0bzaKS+1i6HLiX8wSS9qKFbq3Bvl29grjYKwebGE5STdimgOR6yB6lfxYuHEWAOb7TkxaRXxoYGDPHYJfjFZS7B+BMyRGhnCX09rUChPeTq6EKRp2X5CNzvu7+pQ+kDPW3rsfwIDAQAB'



// 加密
export function rsaencrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}



