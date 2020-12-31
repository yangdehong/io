package com.ydh.redsheep.netty.netty.code.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.util.Pool;
import com.ydh.redsheep.netty.netty.model.MessageBO;
import de.javakaffee.kryoserializers.*;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
* Kryo序列化
* @author : yangdehong
* @date : 2019-10-18 11:02
*/
public class KryoSerializer {

//    private final Class<I> ct;
    /**
     * 由于Kryo不是线程安全的，并且构建和配置Kryo实例相对昂贵，因此在多线程环境中，可以考虑ThreadLocal或pooling。
     */
//    ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
//        @Override
//        protected Kryo initialValue() {
//            return createKryo();
//        }
//    };
    static Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 8) {
        @Override
        protected Kryo create () {
            return createKryo();
        }
    };

//    public KryoSerializer(Class<I> ct) {
//        this.ct = ct;
//    }
//
//    public Class<I> getCt() {
//        return ct;
//    }

    private static Kryo getKryo() {
        return kryoPool.obtain();
    }

    public static void serialize(Object object, ByteBuf out) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        try {
            Kryo kryo = getKryo();
            kryo.writeObjectOrNull(output, object, object.getClass());
            output.flush();
            byte[] bytes = baos.toByteArray();
            int dataLength = bytes.length;
            out.writeInt(dataLength);
            out.writeBytes(bytes);
        } finally {
            output.close();
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static Object deserialize(ByteBuf in, Class<?> clazz) {
        // 标记当前readIndex的位置
        in.markReaderIndex();
        // 读取传送过来的消息长度，ByteBuf的 readInt() 方法会让它的readIndex+4
        int dataLength = in.readInt();
        // 至此，读取到一条完整报文
        byte[] body = new byte[dataLength];
        in.readBytes(body);
        ByteArrayInputStream bais = new ByteArrayInputStream(body);
        Input input = new Input(bais);
        try {
            Kryo kryo = getKryo();
            return kryo.readObjectOrNull(input, clazz);
        } finally {
            input.close();
            try {
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Kryo createKryo() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
//        kryo.register(ct, new BeanSerializer(kryo, ct));
        kryo.register(MessageBO.class);
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
        kryo.register(InvocationHandler.class, new JdkProxySerializer());
        kryo.register(BigDecimal.class, new DefaultSerializers.BigDecimalSerializer());
        kryo.register(BigInteger.class, new DefaultSerializers.BigIntegerSerializer());
        kryo.register(Pattern.class, new RegexSerializer());
        kryo.register(BitSet.class, new BitSetSerializer());
        kryo.register(URI.class, new URISerializer());
        kryo.register(UUID.class, new UUIDSerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);

        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(HashSet.class);
        kryo.register(TreeSet.class);
        kryo.register(Hashtable.class);
        kryo.register(Date.class);
        kryo.register(Calendar.class);
        kryo.register(ConcurrentHashMap.class);
        kryo.register(SimpleDateFormat.class);
        kryo.register(GregorianCalendar.class);
        kryo.register(Vector.class);
        kryo.register(BitSet.class);
        kryo.register(StringBuffer.class);
        kryo.register(StringBuilder.class);
        kryo.register(Object.class);
        kryo.register(Object[].class);
        kryo.register(String[].class);
        kryo.register(byte[].class);
        kryo.register(char[].class);
        kryo.register(int[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);
        return kryo;
    }

}
