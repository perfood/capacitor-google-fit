import Foundation

@objc public class GoogleFit: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
