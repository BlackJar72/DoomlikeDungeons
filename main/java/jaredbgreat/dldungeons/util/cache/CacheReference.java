package jaredbgreat.dldungeons.util.cache;

import java.lang.ref.WeakReference;

public class CacheReference<T> extends WeakReference<T> {
	private final WeakCache owner;

	public CacheReference(T referent, WeakCache owner) {
		super(referent);
		this.owner = owner;
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		owner.reduce();
		super.finalize();
	}

}
