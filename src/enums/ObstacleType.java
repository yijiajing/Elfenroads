package enums;

// We have this enum because all counterUnit must be assigned to a type
// Tree Obstacles are the only type of Obstacle in Elfenland - they can be placed on roads (not rivers or lakes)
// Sea Monster is added to Elfengold - it can be placed on rivers and lakes only
public enum ObstacleType implements CounterUnitType{
	TREE, SEAMONSTER, EGTREE
}
